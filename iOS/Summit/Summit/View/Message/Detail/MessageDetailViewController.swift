//
//  MessageDetailViewController.swift
//  Summit
//
//  Created by Leapfrog-Software on 2018/04/30.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class MessageDetailViewController: KeyboardRespondableViewController {
    
    @IBOutlet private weak var tableView: UITableView!
    @IBOutlet private weak var textField: UITextField!
    @IBOutlet private weak var inputViewBottomConstraint: NSLayoutConstraint!
    
    private var userData: UserData!
    private var messages = [MessageData]()
    private var dummyLeftCell: MessageDetailLeftTableViewCell?
    private var dummyRightCell: MessageDetailRightTableViewCell?
    private var temporaryMessageDatas = [MessageData]()
    
    func set(userData: UserData) {
        self.userData = userData
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.dummyLeftCell = self.tableView.dequeueReusableCell(withIdentifier: "MessageDetailLeftTableViewCell") as? MessageDetailLeftTableViewCell
        self.dummyRightCell = self.tableView.dequeueReusableCell(withIdentifier: "MessageDetailRightTableViewCell") as? MessageDetailRightTableViewCell
        
        self.refresh()
    }
    
    func refresh() {
        
        let messages = MessageRequester.shared.query(userId: self.userData.userId).filter { messageData -> Bool in
            return messageData.senderId == SaveData.shared.userId || messageData.receiverId == SaveData.shared.userId
        }
        self.messages = messages.sorted(by: { (msg1, msg2) -> Bool in
            return msg1.datetime < msg2.datetime
        })
        self.tableView.reloadData()
    }
    
    @IBAction func didExitTextField(_ sender: Any) {
        self.view.endEditing(true)
    }
    
    @IBAction func onTapSend(_ sender: Any) {
        
        self.view.endEditing(true)
        
        guard let message = self.textField.text, message.count > 0 else {
            return
        }
        
        MessageRequester.post(targetId: self.userData.userId, message: message, completion: { [weak self] result in
            if result {
                MessageRequester.shared.fetch(completion: { [weak self] result in
                    if result {
                        self?.temporaryMessageDatas.removeAll()
                        self?.refresh()
                    } else {
                        // TODO
                    }
                })
            } else {
                // TODO
            }
        })
        self.textField.text = ""

        let temporaryMessageData = MessageData(senderId: SaveData.shared.userId, message: message, datetime: Date())
        self.temporaryMessageDatas.append(temporaryMessageData)
        let height = self.dummyRightCell?.height(data: temporaryMessageData) ?? 0
        let offset = self.tableView.contentSize.height - self.tableView.frame.size.height + height
        self.tableView.setContentOffset(CGPoint(x: 0, y: offset), animated: false)
        self.tableView.reloadData()
    }
    
    @IBAction func onTapBack(_ sender: Any) {
        self.pop(animationType: .horizontal)
    }
    
    override func animate(with: KeyboardAnimation) {
        self.inputViewBottomConstraint.constant = with.height
        UIView.animate(withDuration: with.duration, delay: 0, options: with.curve, animations: { [weak self] in
            self?.view.layoutIfNeeded()
        })
    }
}

extension MessageDetailViewController: UITableViewDataSource, UITableViewDelegate {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.messages.count + self.temporaryMessageDatas.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let message = (self.messages + self.temporaryMessageDatas)[indexPath.row]
        if message.senderId == SaveData.shared.userId {
            let cell = tableView.dequeueReusableCell(withIdentifier: "MessageDetailRightTableViewCell", for: indexPath) as! MessageDetailRightTableViewCell
            let isTemporary = indexPath.row > self.messages.count
            cell.configure(data: message, isTemporary: isTemporary)
            return cell
        } else {
            let cell = tableView.dequeueReusableCell(withIdentifier: "MessageDetailLeftTableViewCell", for: indexPath) as! MessageDetailLeftTableViewCell
            cell.configure(data: message)
            return cell
        }
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        
        let messageData = (self.messages + self.temporaryMessageDatas)[indexPath.row]
        if messageData.senderId == SaveData.shared.userId {
            return self.dummyRightCell?.height(data: messageData) ?? 0
        } else {
            return self.dummyLeftCell?.height(data: messageData) ?? 0
        }
    }
}
