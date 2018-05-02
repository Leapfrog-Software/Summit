//
//  AttendChatLeftTableViewCell.swift
//  Summit
//
//  Created by Leapfrog-Software on 2018/05/02.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class AttendChatLeftTableViewCell: UITableViewCell {

    struct Const {
        static let bottomMargin = CGFloat(26)
    }
    
    @IBOutlet private weak var faceImageView: UIImageView!
    @IBOutlet private weak var baloonView: UIView!
    @IBOutlet private weak var messageLabel: UILabel!
    @IBOutlet private weak var dateLabel: UILabel!
    
    private var chatId = ""
    private var didTap: (() -> ())?
    
    override func prepareForReuse() {
        super.prepareForReuse()
        
        self.didTap = nil
        
        ImageStorage.shared.cancelRequest(imageView: self.faceImageView)
    }
    
    func configure(data: ChatData, didTap: (() -> ())? = nil) {
        
        ImageStorage.shared.fetch(url: Constants.UserImageDirectory + data.senderId, imageView: self.faceImageView)
        self.messageLabel.text = data.chat
        
        let today = Date()
        if data.datetime.isSameDay(with: today) {
            self.dateLabel.text = DateFormatter(dateFormat: "HH:mm").string(from: data.datetime)
        } else if data.datetime.isSameYear(with: today) {
            self.dateLabel.text = DateFormatter(dateFormat: "M月d日 HH:mm").string(from: data.datetime)
        } else {
            self.dateLabel.text = DateFormatter(dateFormat: "yyyy年M月d日 HH:mm").string(from: data.datetime)
        }
        
        self.chatId = data.id
        self.didTap = didTap
    }
    
    func getChatId() -> String {
        return self.chatId
    }
    
    func height(data: ChatData) -> CGFloat {
        
        self.configure(data: data)
        
        self.setNeedsLayout()
        self.layoutIfNeeded()
        
        return self.baloonView.frame.origin.y + self.baloonView.frame.size.height + Const.bottomMargin
    }
    
    @IBAction func onTapFace(_ sender: Any) {
        self.didTap?()
    }
}
