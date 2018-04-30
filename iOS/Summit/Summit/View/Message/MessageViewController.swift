//
//  MessageViewController.swift
//  Summit
//
//  Created by Leapfrog-Software on 2018/04/29.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class MessageViewController: UIViewController {
    
    @IBOutlet private weak var tableView: UITableView!
    
    private var users = [UserData]()
    
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
        self.users = userList.sorted(by: { (user1, user2) -> Bool in
            guard let lastDatetime1 = latestMessageDatetime(messages: MessageRequester.shared.query(userId: user1.userId)) else {
                return false
            }
            guard let lastDatetime2 = latestMessageDatetime(messages: MessageRequester.shared.query(userId: user2.userId)) else {
                return true
            }
            return lastDatetime1 > lastDatetime2
        })
        
        self.tableView.reloadData()
    }
}

extension MessageViewController: UITableViewDataSource, UITableViewDelegate {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.users.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell = tableView.dequeueReusableCell(withIdentifier: "MessageTableViewCell", for: indexPath) as! MessageTableViewCell
        cell.configure(userData: self.users[indexPath.row])
        return cell
    }
    
    public func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        let messageDetailViewController = self.viewController(storyboard: "Message", identifier: "MessageDetailViewController") as! MessageDetailViewController
        messageDetailViewController.set(userData: self.users[indexPath.row])
        self.tabbarViewController()?.stack(viewController: messageDetailViewController, animationType: .horizontal)
    }
}
