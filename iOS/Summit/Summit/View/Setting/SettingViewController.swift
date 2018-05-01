//
//  SettingViewController.swift
//  Summit
//
//  Created by Leapfrog-Software on 2018/04/29.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class SettingViewController: UIViewController {
    
    @IBOutlet private weak var faceImageView: UIImageView!
    @IBOutlet private weak var nameLabel: UILabel!
    @IBOutlet private weak var companyLabel: UILabel!
    @IBOutlet private weak var positionLabel: UILabel!

    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.initContents()
    }
    
    private func initContents() {
        
        guard let myUserData = UserRequester.shared.myUserData() else {
            return
        }
        
        ImageStorage.shared.fetch(url: Constants.UserImageDirectory + myUserData.userId, imageView: self.faceImageView)
        
        if myUserData.nameLast.count == 0 && myUserData.nameFirst.count == 0 {
            self.nameLabel.text = "(no name)"
            self.nameLabel.textColor = .settingNoName
        } else {
            self.nameLabel.text = myUserData.nameLast + " " + myUserData.nameFirst
            self.nameLabel.textColor = .black
        }
        
        self.companyLabel.text = myUserData.company
        self.positionLabel.text = myUserData.position
    }
    
    @IBAction func onTapProfile(_ sender: Any) {
        let profile = self.viewController(storyboard: "Setting", identifier: "SettingProfileViewController") as! SettingProfileViewController
        self.tabbarViewController()?.stack(viewController: profile, animationType: .horizontal)
    }
    
    @IBAction func onTapTerm(_ sender: Any) {
        
    }
    
    @IBAction func onTapPrivacyPolicy(_ sender: Any) {
        
    }
}
