//
//  AttendMatchViewController.swift
//  Summit
//
//  Created by Leapfrog-Software on 2018/05/01.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class AttendMatchViewController: KeyboardRespondableViewController {

    @IBOutlet private weak var verticalCenterConstraint: NSLayoutConstraint!
    @IBOutlet private weak var containerView: UIView!
    @IBOutlet private weak var titleLabel: UILabel!
    @IBOutlet private weak var faceImageView: UIImageView!
    @IBOutlet private weak var matchNameLabel: UILabel!
    @IBOutlet private weak var chatTextView: UITextView!
    @IBOutlet private weak var sendButton: UIButton!
    
    private var scheduleData: ScheduleData!
    private var matchUserData: UserData!
    private var tableId: String!
    private var completion: (() -> ())!

    func set(scheduleData: ScheduleData, userData: UserData, tableId: String, completion: @escaping (() -> ())) {
        self.scheduleData = scheduleData
        self.matchUserData = userData
        self.tableId = tableId
        self.completion = completion
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.titleLabel.text = self.scheduleData.title + "の開始"
        ImageStorage.shared.fetch(url: Constants.UserImageDirectory + self.matchUserData.userId, imageView: self.faceImageView)
        self.matchNameLabel.text = self.matchUserData.nameLast + self.matchUserData.nameFirst + "さんとマッチしました！"
        
        self.setSendButtonEnabled(false)
        
        self.verticalCenterConstraint.constant = UIScreen.main.bounds.size.height
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        
        self.verticalCenterConstraint.constant = 0
        UIView.animate(withDuration: 0.6, delay: 0, usingSpringWithDamping: 0.8, initialSpringVelocity: 3, options: .curveEaseInOut, animations: {
            self.view.layoutIfNeeded()
        })
    }
    
    private func close() {
        
        self.verticalCenterConstraint.constant = UIScreen.main.bounds.size.height
        UIView.animate(withDuration: 0.6, delay: 0, usingSpringWithDamping: 0.8, initialSpringVelocity: 3, options: .curveEaseInOut, animations: {
            self.view.layoutIfNeeded()
        }, completion: { [weak self] _ in
            self?.pop(animationType: .none)
        })
    }
    
    override func animate(with: KeyboardAnimation) {
        
        if with.height > 0 {
            let contentsBottom = UIScreen.main.bounds.size.height / 2 - self.containerView.frame.size.height / 2
            let liftupHeight = with.height - contentsBottom
            self.verticalCenterConstraint.constant = -liftupHeight
        } else {
            self.verticalCenterConstraint.constant = 0
        }
        
        UIView.animate(withDuration: 0.2) {
            self.view.layoutIfNeeded()
        }
    }
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        self.view.endEditing(true)
    }
    
    @IBAction func onTapSend(_ sender: Any) {
        
        self.view.endEditing(true)
        
        let chat = self.chatTextView.text ?? ""
        if chat.count == 0 {
            return
        }
        
        let requester = AttendRequester()
        requester.initialize(scheduleId: self.scheduleData.id)
        
        let chatId = DateFormatter(dateFormat: "yyyyMMddHHmmssSSS").string(from: Date())
        requester.postChat(chatId: chatId, tableId: self.tableId, chat: chat, completion: { result in
            if result {
                let saveData = SaveData.shared
                saveData.sentFirstMessageScheduleIds.append(self.scheduleData.id)
                saveData.save()
                
                self.completion()
                self.close()
            } else {
                Dialog.show(style: .error, title: "エラー", message: "通信に失敗しました", actions: [DialogAction(title: "OK", action: nil)])
            }
        })
    }
}

extension AttendMatchViewController: UITextViewDelegate {
    
    func textViewDidChange(_ textView: UITextView) {
        if textView.text.count >= 0 {
            self.setSendButtonEnabled(true)
        } else {
            self.setSendButtonEnabled(false)
        }
    }
    
    private func setSendButtonEnabled(_ enabled: Bool) {
        if enabled {
            self.sendButton.backgroundColor = UIColor.matchMessageSendButtonActive
            self.sendButton.isEnabled = true
        } else {
            self.sendButton.backgroundColor = UIColor.matchMessageSendButtonInactive
            self.sendButton.isEnabled = false
        }
    }
}
