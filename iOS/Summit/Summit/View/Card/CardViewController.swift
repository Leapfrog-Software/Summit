//
//  CardViewController.swift
//  Summit
//
//  Created by Leapfrog-Software on 2018/04/29.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class CardViewController: UIViewController {

    struct Const {
        static let kanaTableCellHeight = CGFloat(48)
        static let cardTableCellHeight = CGFloat(116)
    }
    
    struct CellData {
        let kana: String?
        let user: UserData?
    }
    
    @IBOutlet private weak var cardTableView: UITableView!
    @IBOutlet private weak var cardIndexBaseView: UIView!
    @IBOutlet private weak var noDataLabel: UILabel!
    
    private weak var cardIndexView: CardIndexView?
    private var cellDatas = [CellData]()
    private var kanaOffsets = [(kana: String, offset: CGFloat)]()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.setCardIndexView()
        self.reload()
    }
    
    private func setCardIndexView() {
        
        let cardIndexView = UINib(nibName: "CardIndexView", bundle: nil).instantiate(withOwner: nil, options: nil).first as! CardIndexView
        self.cardIndexBaseView.addSubview(cardIndexView)
        cardIndexView.translatesAutoresizingMaskIntoConstraints = false
        cardIndexView.leadingAnchor.constraint(equalTo: self.cardIndexBaseView.leadingAnchor).isActive = true
        cardIndexView.trailingAnchor.constraint(equalTo: self.cardIndexBaseView.trailingAnchor).isActive = true
        cardIndexView.topAnchor.constraint(equalTo: self.cardIndexBaseView.topAnchor).isActive = true
        cardIndexView.bottomAnchor.constraint(equalTo: self.cardIndexBaseView.bottomAnchor).isActive = true
        
        cardIndexView.set(index: -1)
        
        self.cardIndexView = cardIndexView
    }
    
    func reload() {
        
        guard let myUserData = UserRequester.shared.myUserData() else {
            return
        }
        let users = myUserData.cards.compactMap { UserRequester.shared.query(userId: $0) }
        let sortedUsers = users.sorted { (user1, user2) -> Bool in
            return KanaUtils.compare(s1: user1.kanaLast + user1.kanaFirst, s2: user2.kanaLast + user2.kanaFirst)
        }
        
        self.cellDatas.removeAll()
        self.kanaOffsets.removeAll()
        var currentKanaIndex = -1
        var currentOffset = CGFloat(0)
        
        sortedUsers.forEach { user in
            let userKanaPrefix = String(user.kanaLast.prefix(1))
            guard let userKanaIndex = KanaUtils.columnIndex(of: userKanaPrefix) else {
                return
            }
            if (currentKanaIndex == -1) {
                self.cardIndexView?.set(index: userKanaIndex)
            }
            if userKanaIndex != currentKanaIndex {
                let kana = KanaUtils.kanas[userKanaIndex][0]
                self.cellDatas.append(CellData(kana: kana, user: nil))
                currentKanaIndex = userKanaIndex
                self.kanaOffsets.append((kana: kana, offset: currentOffset))
                currentOffset += Const.kanaTableCellHeight
            }
            self.cellDatas.append(CellData(kana: nil, user: user))
            currentOffset += Const.cardTableCellHeight
        }
        
        self.cardTableView.isHidden = self.cellDatas.isEmpty
        self.noDataLabel.isHidden = !self.cellDatas.isEmpty
    }
}

extension CardViewController: UITableViewDataSource, UITableViewDelegate {
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        let cellData = self.cellDatas[indexPath.row]
        return (cellData.kana != nil) ? Const.kanaTableCellHeight : Const.cardTableCellHeight
    }

    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.cellDatas.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cellData = self.cellDatas[indexPath.row]
        if let kana = cellData.kana {
            let cell = tableView.dequeueReusableCell(withIdentifier: "CardKanaTableViewCell", for: indexPath) as! CardKanaTableViewCell
            cell.configure(kana: kana)
            return cell
        } else {
            let cell = tableView.dequeueReusableCell(withIdentifier: "CardUserTableViewCell", for: indexPath) as! CardUserTableViewCell
            cell.configure(userData: cellData.user!)
            return cell
        }
    }
    
    public func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        tableView.deselectRow(at: indexPath, animated: true)
        
        if let userData = self.cellDatas[indexPath.row].user {
            let cardDetailViewController = self.viewController(storyboard: "Card", identifier: "CardDetailViewController") as! CardDetailViewController
            cardDetailViewController.set(userData: userData, showSendCard: false)
            self.tabbarViewController()?.stack(viewController: cardDetailViewController, animationType: .horizontal)
        }
    }
    
    func scrollViewDidScroll(_ scrollView: UIScrollView) {
        
        for i in 0..<self.kanaOffsets.count {
            if i == self.kanaOffsets.count - 1 {
                if let index = KanaUtils.columnIndex(of: self.kanaOffsets[i].kana) {
                    self.cardIndexView?.set(index: index)
                }
            } else {
                let nextOffset = self.kanaOffsets[i + 1]
                if scrollView.contentOffset.y < nextOffset.offset {
                    if let index = KanaUtils.columnIndex(of: self.kanaOffsets[i].kana) {
                        self.cardIndexView?.set(index: index)
                        return
                    }
                }
            }
        }
    }
}
