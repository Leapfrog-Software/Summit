//
//  MessageViewController.swift
//  Summit
//
//  Created by Leapfrog-Software on 2018/04/29.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class MessageViewController: UIViewController {
    
    struct CellData {
        let userData: UserData
        let exist: Bool
        let dateString: String
    }
    
    @IBOutlet private weak var tableView: UITableView!
    @IBOutlet private weak var noDataLabel: UILabel!
    
    private var cellDatas = [CellData]()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.refresh()
    }
    
    func refresh() {
        
        var targetUserIds = [String]()
        MessageRequester.shared.dataList.forEach { messageData in
            let targetUserId: String
            if messageData.senderId == SaveData.shared.userId {
                targetUserId = messageData.receiverId
            } else {
                targetUserId = messageData.senderId
            }
            if !targetUserIds.contains(targetUserId) {
                targetUserIds.append(targetUserId)
            }
        }
        
        func latestMessageDatetime(messages: [MessageData]) -> Date? {
            let sorted = messages.sorted { (msg1, msg2) -> Bool in
                return msg1.datetime > msg2.datetime
            }
            return sorted.last?.datetime
        }
        
        let userList = targetUserIds.compactMap { UserRequester.shared.query(userId: $0) }
        let sortedUserList = userList.sorted(by: { (user1, user2) -> Bool in
            guard let lastDatetime1 = latestMessageDatetime(messages: MessageRequester.shared.query(userId: user1.userId)) else {
                return false
            }
            guard let lastDatetime2 = latestMessageDatetime(messages: MessageRequester.shared.query(userId: user2.userId)) else {
                return true
            }
            return lastDatetime1 > lastDatetime2
        })
        
        let today = Date()
        sortedUserList.forEach { userData in
            var dateString = ""
            var exist = false
            if let dateTime = latestMessageDatetime(messages: MessageRequester.shared.query(userId: userData.userId)) {
                if dateTime.isSameDay(with: today) {
                    dateString = DateFormatter(dateFormat: "HH:mm").string(from: dateTime)
                } else if dateTime.isSameYear(with: today) {
                    dateString = DateFormatter(dateFormat: "M月d日\nHH:mm").string(from: dateTime)
                } else {
                    dateString = DateFormatter(dateFormat: "yyyy年M月d日\nHH:mm").string(from: dateTime)
                }
                if dateTime.timeIntervalSinceNow > -60 * 60 * 24 {
                    exist = true
                }
            }
            self.cellDatas.append(CellData(userData: userData, exist: exist, dateString: dateString))
        }
        
        self.tableView.reloadData()
        
        self.tableView.isHidden = self.cellDatas.isEmpty
        self.noDataLabel.isHidden = !self.cellDatas.isEmpty
    }
}

extension MessageViewController: UITableViewDataSource, UITableViewDelegate {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.cellDatas.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell = tableView.dequeueReusableCell(withIdentifier: "MessageTableViewCell", for: indexPath) as! MessageTableViewCell
        let cellData = self.cellDatas[indexPath.row]
        cell.configure(userData: cellData.userData, exist: cellData.exist, dateString: cellData.dateString)
        return cell
    }
    
    public func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        let messageDetailViewController = self.viewController(storyboard: "Message", identifier: "MessageDetailViewController") as! MessageDetailViewController
        messageDetailViewController.set(userData: self.cellDatas[indexPath.row].userData)
        self.tabbarViewController()?.stack(viewController: messageDetailViewController, animationType: .horizontal)
    }
}
