//
//  SettingProfileNameViewController.swift
//  Summit
//
//  Created by Leapfrog-Software on 2018/05/01.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class SettingProfileNameViewController: UIViewController {

    @IBOutlet private weak var settingTitleLabel: UILabel!
    @IBOutlet private weak var lastTextField: UITextField!
    @IBOutlet private weak var firstTextField: UITextField!
    
    private var isKana: Bool!
    private var defaultLast: String!
    private var defaultFirst: String!
    private var completion: ((String, String) -> ())!
    
    func set(defaultLast: String, defaultFirst: String, isKana: Bool, completion: @escaping ((String, String) -> ())) {
        self.isKana = isKana
        self.defaultLast = defaultLast
        self.defaultFirst = defaultFirst
        self.completion = completion
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()

        self.lastTextField.text = self.defaultLast
        self.firstTextField.text = self.defaultFirst
        
        if self.isKana {
            self.settingTitleLabel.text = "氏名 (かな)"
        } else {
            self.settingTitleLabel.text = "氏名"
        }
    }
    
    @IBAction func onTapBack(_ sender: Any) {
        
        let lastString = self.lastTextField.text ?? ""
        let firstString = self.firstTextField.text ?? ""
        
        if self.defaultLast == lastString && self.defaultFirst == firstString {
            self.pop(animationType: .horizontal)
        } else {
            if lastString.count + firstString.count > 12 {
                // TODO
                
            } else {
                self.completion(lastString, firstString)
                self.pop(animationType: .horizontal)
            }
        }
    }
}
