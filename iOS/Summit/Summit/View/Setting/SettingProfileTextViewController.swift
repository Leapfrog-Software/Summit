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
    private var placeholder: String!
    private var completion: ((String) -> ())!
    
    func set(title: String, defaultString: String, placeholder: String, completion: @escaping ((String) -> ())) {
        self.settingTitle = title
        self.defaultString = defaultString
        self.completion = completion
        self.placeholder = placeholder
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.settingTitleLabel.text = self.settingTitle
        self.textField.text = self.defaultString
        self.textField.placeholder = self.placeholder
    }
    
    @IBAction func onTapBack(_ sender: Any) {
        
        let text = self.textField.text ?? ""
        
        if self.defaultString == text {
            self.pop(animationType: .horizontal)
        } else {
            if text.count > 12 {
                Dialog.show(style: .error, title: "入力エラー", message: "12文字以内で入力してください", actions: [DialogAction(title: "OK", action: nil)])
            } else {
                self.completion(text)
                self.pop(animationType: .horizontal)
            }
        }
    }
}
