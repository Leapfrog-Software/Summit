//
//  AttendMemberListViewController.swift
//  Summit
//
//  Created by Leapfrog-Software on 2018/05/03.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class AttendMemberListViewController: UIViewController {

    private var userDatas = [UserData]()
    
    func set(userIds: [String]) {

        self.userDatas = userIds.compactMap { UserRequester.shared.query(userId: $0) }
    }
    
    @IBAction func onTapBack(_ sender: Any) {
        self.pop(animationType: .horizontal)
    }
}

extension AttendMemberListViewController: UITableViewDataSource, UITableViewDelegate {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.userDatas.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell = tableView.dequeueReusableCell(withIdentifier: "AttendMemberListTableViewCell", for: indexPath) as! AttendMemberListTableViewCell
        cell.configure(userData: self.userDatas[indexPath.row])
        return cell
    }
    
    public func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        tableView.deselectRow(at: indexPath, animated: true)
        
        let cardDetailViewController = self.viewController(storyboard: "Card", identifier: "CardDetailViewController") as! CardDetailViewController
        cardDetailViewController.set(userData: self.userDatas[indexPath.row], showSendCard: true)
        self.stack(viewController: cardDetailViewController, animationType: .horizontal)
    }
}
