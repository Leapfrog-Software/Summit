//
//  SettingProfileTextViewController.swift
//  Summit
//
//  Created by Leapfrog-Software on 2018/05/01.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class SettingProfileTextViewController: UIViewController {

    @IBOutlet private weak var settingTitleLabel: UILabel!
    @IBOutlet private weak var textField: UITextField!
    
    private var settingTitle: String!
    private var defaultString: String!
    private var completion: ((String) -> ())!
    
    func set(title: String, defaultString: String, completion: @escaping ((String) -> ())) {
        self.settingTitle = title
        self.defaultString = defaultString
        self.completion = completion
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.settingTitleLabel.text = self.settingTitle
        self.textField.text = self.defaultString
    }
    
    @IBAction func onTapBack(_ sender: Any) {
        
        let text = self.textField.text ?? ""
        
        if self.defaultString == text {
            self.pop(animationType: .horizontal)
        } else {
            if text.count > 12 {
                // TODO
                
            } else {
                self.completion(text)
                self.pop(animationType: .horizontal)
            }
        }
    }
}
