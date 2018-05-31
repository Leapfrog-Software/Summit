//
//  ScheduleDetailViewController.swift
//  Summit
//
//  Created by Leapfrog-Software on 2018/04/30.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class ScheduleDetailViewController: UIViewController {
    
    @IBOutlet private weak var scheduleImageView: UIImageView!
    @IBOutlet private weak var nameLabel: UILabel!
    @IBOutlet private weak var memberCountLabel: UILabel!
    @IBOutlet private weak var dateLabel: UILabel!
    @IBOutlet private weak var providerLabel: UILabel!
    @IBOutlet private weak var descriptionLabel: UILabel!
    @IBOutlet private weak var filterLabel: UILabel!
    @IBOutlet private weak var agreeView: UIView!
    @IBOutlet private weak var agreeViewHeightConstraint: NSLayoutConstraint!
    @IBOutlet private weak var agreeImageView: UIImageView!
    @IBOutlet private weak var reserveButton: UIButton!

    private var scheduleData: ScheduleData!
    private var members = [UserData]()
    private var didAgree = false
    
    func set(scheduleData: ScheduleData) {
        self.scheduleData = scheduleData
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.initContents()
    }
    
    private func initContents() {

        self.members = UserRequester.shared.dataList.filter { userData -> Bool in
            return userData.reserves.contains(self.scheduleData.id)
        }
        
        ImageStorage.shared.fetch(url: Constants.ScheduleImageDirectory + self.scheduleData.id, imageView: self.scheduleImageView)
        
        self.nameLabel.text = self.scheduleData.title
        
        let memberCount = self.members.count
        self.memberCountLabel.text = "(\(memberCount)名)"
        
        self.dateLabel.text = DateFormatter(dateFormat: "M月d日 HH:mm〜").string(from: self.scheduleData.date)
        
        self.providerLabel.text = self.scheduleData.provider
        
        self.descriptionLabel.set(text: self.scheduleData.description, lineHeight: 18)
        self.filterLabel.text = self.scheduleData.filter
        
        if let myUserData = UserRequester.shared.myUserData() {
            if myUserData.reserves.contains(self.scheduleData.id) {
                self.reserveButton.backgroundColor = UIColor.inActiveButton
                self.reserveButton.isEnabled = false
                self.reserveButton.setTitle("予約済みです", for: .normal)
            } else if self.scheduleData.date.addingTimeInterval(-60 * 60).timeIntervalSinceNow < 0 {
                self.reserveButton.backgroundColor = UIColor.inActiveButton
                self.reserveButton.isEnabled = false
                self.reserveButton.setTitle("予約可能日時を過ぎています", for: .normal)
            } else {
                self.reserveButton.backgroundColor = UIColor.activeButton
                self.reserveButton.isEnabled = true
            }
        }
        
        if SaveData.shared.didTermsAgree {
            self.agreeView.isHidden = true
            self.agreeViewHeightConstraint.constant = 0
            self.didAgree = true
        } else {
            self.agreeImageView.isHidden = true
        }
    }
    
    @IBAction func onTapReserve(_ sender: Any) {
        
        if !self.didAgree {
            Dialog.show(style: .error, title: "エラー", message: "利用規約の同意が必要です", actions: [DialogAction(title: "OK", action: nil)])
            return
        }
        let saveData = SaveData.shared
        saveData.didTermsAgree = true
        saveData.save()
        
        guard var myUserData = UserRequester.shared.myUserData() else {
            return
        }
        
        if myUserData.nameLast.isEmpty || myUserData.nameFirst.isEmpty || myUserData.kanaLast.isEmpty || myUserData.kanaFirst.isEmpty {
            Dialog.show(style: .error, title: "プロフィールに未設定項目があります", message: "プロフィールを編集してください", actions: [DialogAction(title: "OK", action: nil)])
            return
        }
        
        myUserData.reserves.append(self.scheduleData.id)
        
        Loading.start()
        
        AccountRequester.updateUser(userData: myUserData, completion: { result in
            if result {
                UserRequester.shared.fetch(completion: { _ in
                    Loading.stop()
                    Dialog.show(style: .success, title: "確認", message: "予約が完了しました", actions: [DialogAction(title: "OK", action: nil)])
                    
                    self.reserveButton.backgroundColor = UIColor.inActiveButton
                    self.reserveButton.isEnabled = false
                    self.reserveButton.setTitle("予約済みです", for: .normal)
                })
                
            } else {
                Loading.stop()
                Dialog.show(style: .error, title: "エラー", message: "通信に失敗しました", actions: [DialogAction(title: "OK", action: nil)])
            }
        })
    }
    
    @IBAction func onTapAgree(_ sender: Any) {
        
        self.didAgree = !self.didAgree
        self.agreeImageView.isHidden = !self.didAgree
    }
    
    @IBAction func onTapTerms(_ sender: Any) {
        let webView = self.viewController(storyboard: "Common", identifier: "WebViewController") as! WebViewController
        webView.set(webPageType: .terms)
        self.stack(viewController: webView, animationType: .horizontal)
    }
    
    @IBAction func onTapClose(_ sender: Any) {
        self.pop(animationType: .horizontal)
    }
}

extension ScheduleDetailViewController: UICollectionViewDelegate, UICollectionViewDataSource {
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return self.members.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "ScheduleDetailMemberCollectionViewCell", for: indexPath) as! ScheduleDetailMemberCollectionViewCell
        cell.configure(userData: self.members[indexPath.row])
        return cell
    }
}
