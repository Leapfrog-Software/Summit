//
//  ScheduleDetailMemberCollectionViewCell.swift
//  Summit
//
//  Created by Leapfrog-Software on 2018/04/30.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class ScheduleDetailMemberCollectionViewCell: UICollectionViewCell {
    
    @IBOutlet private weak var memberImageView: UIImageView!
    
    override func prepareForReuse() {
        super.prepareForReuse()
        
        ImageStorage.shared.cancelRequest(imageView: self.memberImageView)
    }
    
    func configure(userData: UserData) {
        
        ImageStorage.shared.fetch(url: Constants.UserImageDirectory + userData.userId, imageView: self.memberImageView)
    }
}
