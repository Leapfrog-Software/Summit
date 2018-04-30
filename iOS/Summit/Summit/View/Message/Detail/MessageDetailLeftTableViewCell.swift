//
//  MessageDetailLeftTableViewCell.swift
//  Summit
//
//  Created by Leapfrog-Software on 2018/04/30.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

import UIKit

class MessageDetailLeftTableViewCell: UITableViewCell {
    
    struct Const {
        static let bottomMargin = CGFloat(26)
    }
    
    @IBOutlet private weak var faceImageView: UIImageView!
    @IBOutlet private weak var baloonView: UIView!
    @IBOutlet private weak var messageLabel: UILabel!
    @IBOutlet private weak var dateLabel: UILabel!
    
    private var didTap: (() -> ())?
    
    override func prepareForReuse() {
        super.prepareForReuse()
        
        self.didTap = nil
        
        ImageStorage.shared.cancelRequest(imageView: self.faceImageView)
    }
    
    func configure(data: MessageData, didTap: (() -> ())? = nil) {
        
        ImageStorage.shared.fetch(url: Constants.UserImageDirectory + data.senderId, imageView: self.faceImageView)
        self.messageLabel.text = data.message
        self.didTap = didTap
    }
    
    func height(data: MessageData) -> CGFloat {
        
        self.configure(data: data)
        
        self.setNeedsLayout()
        self.layoutIfNeeded()
        
        return self.baloonView.frame.origin.y + self.baloonView.frame.size.height + Const.bottomMargin
    }
    
    @IBAction func onTapFace(_ sender: Any) {
        self.didTap?()
    }
}

