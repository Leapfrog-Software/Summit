//
//  AttendChatViewController.swift
//  Summit
//
//  Created by Leapfrog-Software on 2018/05/02.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

protocol AttendChatDelegate: class {
    func didTapSend(chatId: String, chat: String)
}

class AttendChatViewController: KeyboardRespondableViewController {
    
    struct Const {
        static let chatTopLimit = CGFloat(120)
        static let chatBottomLimit = CGFloat(80)
    }

    @IBOutlet private weak var headerView: UIView!
    @IBOutlet private weak var tableView: UITableView!
    @IBOutlet private weak var textField: UITextField!
    @IBOutlet private weak var inputViewBottomConstraint: NSLayoutConstraint!
    @IBOutlet private weak var memberImageView1: UIImageView!
    @IBOutlet private weak var memberImageView2: UIImageView!
    @IBOutlet private weak var memberImageView3: UIImageView!
    @IBOutlet private weak var memberImageView4: UIImageView!
    @IBOutlet private weak var memberImageView5: UIImageView!
    @IBOutlet private weak var memberCountLabel: UILabel!
    
    weak var delegate: AttendChatDelegate?
    
    private var tableId = ""
    private var chatList = [ChatData]()
    private var tmpChatIds = [String]()
    private var touchOffset: CGPoint?
    private var userIds = [String]()
    
    private var dummyLeftCell: AttendChatLeftTableViewCell?
    private var dummyRightCell: AttendChatRightTableViewCell?
    
    func set(tableId: String, chatList: [ChatData], userIds: [String]) {

        if self.tableId == tableId && chatList.count == self.chatList.count - self.tmpChatIds.count {
            return
        }
        
        if self.tableId == tableId {
            self.resetChatList(tableId: tableId, chatList: chatList, userIds: userIds)
        } else {
            UIView.animate(withDuration: 0.2, animations: {
                self.tableView.alpha = 0
            }, completion: { [weak self] _ in
                self?.resetChatList(tableId: tableId, chatList: chatList, userIds: userIds)
                UIView.animate(withDuration: 0.2, animations: {
                    self?.tableView.alpha = 1
                })
            })
        }
    }
    
    private func resetChatList(tableId: String, chatList: [ChatData], userIds: [String]) {
        
        self.tmpChatIds.removeAll()
        self.chatList = chatList
        self.tableView.reloadData()
        if self.tableId != tableId || self.isVisibleBottom() == true {
            self.scrollToBottom(animated: (self.tableId == tableId))
        }
        self.tableId = tableId
        
        for i in 0..<5 {
            let imageView = [self.memberImageView1, self.memberImageView2, self.memberImageView3, self.memberImageView4, self.memberImageView5][i]!
            if userIds.count > i {
                imageView.isHidden = false
                imageView.image = nil
                ImageStorage.shared.fetch(url: Constants.UserImageDirectory + userIds[i], imageView: imageView)
            } else {
                imageView.isHidden = true
            }
        }
        self.memberCountLabel.text = "\(userIds.count)"
        
        self.userIds = userIds
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.dummyLeftCell = self.tableView.dequeueReusableCell(withIdentifier: "AttendChatLeftTableViewCell") as? AttendChatLeftTableViewCell
        self.dummyRightCell = self.tableView.dequeueReusableCell(withIdentifier: "AttendChatRightTableViewCell") as? AttendChatRightTableViewCell
    }
    
    private func alignFrame() {

        let targetY: CGFloat
        
        if self.view.frame.origin.y < UIScreen.main.bounds.size.height / 2 {
            targetY = Const.chatTopLimit
        } else {
            targetY = UIScreen.main.bounds.size.height - Const.chatBottomLimit
        }
        
        UIView.animate(withDuration: 0.2) {
            self.view.frame = CGRect(x: 0, y: targetY, width: self.view.frame.size.width, height: self.view.frame.size.height)
        }
    }

    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        
        self.view.endEditing(true)
        
        guard let location = touches.first?.location(in: UIApplication.shared.keyWindow) else {
            return
        }
        let headerFrame = self.headerView.absoluteFrame()
        if headerFrame.contains(location) {
            self.touchOffset = CGPoint(x: location.x - headerFrame.origin.x,
                                       y: location.y - headerFrame.origin.y)
        } else {
            self.touchOffset = nil
        }
    }
    
    override func touchesMoved(_ touches: Set<UITouch>, with event: UIEvent?) {
        
        guard let touchOffset = self.touchOffset,
            let location = touches.first?.location(in: UIApplication.shared.keyWindow) else {
            return
        }
        
        let offsetY = location.y - touchOffset.y

        if offsetY < Const.chatTopLimit || UIScreen.main.bounds.size.height - offsetY < Const.chatBottomLimit {
            return
        }
        self.view.frame = CGRect(x: 0,
                                 y: offsetY,
                                 width: self.view.frame.size.width,
                                 height: self.view.frame.size.height)
    }
    
    override func touchesEnded(_ touches: Set<UITouch>, with event: UIEvent?) {
        self.alignFrame()
        self.touchOffset = nil
    }
    
    override func touchesCancelled(_ touches: Set<UITouch>, with event: UIEvent?) {
        self.alignFrame()
        self.touchOffset = nil
    }
    
    override func animate(with: KeyboardAnimation) {
        
        self.inputViewBottomConstraint.constant = with.height
        UIView.animate(withDuration: with.duration, delay: 0, options: with.curve, animations: {
            self.view.layoutIfNeeded()
        })
        
        self.scrollToBottom(animated: true)
    }
    
    private func isVisibleBottom() -> Bool {
        
        var lastChatId = ""
        if self.chatList.count >= 2 {
            lastChatId = self.chatList[self.chatList.count - 2].id
        }
        for i in 0..<self.tableView.visibleCells.count {
            let cell = self.tableView.visibleCells[i]
            if let leftCell = cell as? AttendChatLeftTableViewCell {
                if lastChatId == leftCell.getChatId() {
                    return true
                }
            } else if let rightCell = cell as? AttendChatRightTableViewCell {
                if lastChatId == rightCell.getChatId() {
                    return true
                }
            }
        }
        return false
    }
    
    private func scrollToBottom(animated: Bool) {

        DispatchQueue.main.asyncAfter(deadline: .now() + 0.1) {
            if self.tableView.contentSize.height > self.tableView.frame.size.height {
                let offset = CGPoint(x: 0, y: self.tableView.contentSize.height - self.tableView.frame.size.height)
                self.tableView.setContentOffset(offset, animated: animated)
            }
        }
    }
    
    @IBAction func didExitMessage(_ sender: Any) {
        self.view.endEditing(true)
    }
    
    @IBAction func onTapMembers(_ sender: Any) {
        
        let memberList = self.viewController(storyboard: "Attend", identifier: "AttendMemberListViewController") as! AttendMemberListViewController
        memberList.set(userIds: self.userIds)
        self.parent?.stack(viewController: memberList, animationType: .horizontal)
    }
    
    @IBAction func onTapSend(_ sender: Any) {
        
        let chat = self.textField.text ?? ""
        if chat.isEmpty {
            return
        }
        
        let chatId = DateFormatter(dateFormat: "yyyyMMddHHmmssSSS").string(from: Date())
        let chatData = ChatData(id: chatId, senderId: SaveData.shared.userId, tableId: self.tableId, datetime: Date(), chat: chat)
        self.chatList.append(chatData)
        self.tmpChatIds.append(chatId)
        self.tableView.reloadData()
        
        self.scrollToBottom(animated: true)
        
        self.delegate?.didTapSend(chatId: chatId, chat: chat)
        self.textField.text = ""
    }
}

extension AttendChatViewController: UITableViewDataSource, UITableViewDelegate {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.chatList.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let chatData = self.chatList[indexPath.row]
        if chatData.senderId == SaveData.shared.userId {
            let cell = tableView.dequeueReusableCell(withIdentifier: "AttendChatRightTableViewCell", for: indexPath) as! AttendChatRightTableViewCell
            let isTemporary = self.tmpChatIds.contains(chatData.id)
            cell.configure(data: chatData, isTemporary: isTemporary)
            return cell
        } else {
            let cell = tableView.dequeueReusableCell(withIdentifier: "AttendChatLeftTableViewCell", for: indexPath) as! AttendChatLeftTableViewCell
            cell.configure(data: chatData)
            return cell
        }
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        
        let chatData = self.chatList[indexPath.row]
        if chatData.senderId == SaveData.shared.userId {
            return self.dummyRightCell?.height(data: chatData) ?? 0
        } else {
            return self.dummyLeftCell?.height(data: chatData) ?? 0
        }
    }
}

