//
//  SettingProfileMessageViewController.swift
//  Summit
//
//  Created by Leapfrog-Software on 2018/05/01.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class SettingProfileMessageViewController: UIViewController {

    @IBOutlet private weak var textView: UITextView!
    
    private var defaultString: String!
    private var completion: ((String) -> ())!
    
    func set(defaultString: String, completion: @escaping ((String) -> ())) {
        self.defaultString = defaultString
        self.completion = completion
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.textView.text = self.defaultString
    }
    
    @IBAction func onTapBack(_ sender: Any) {
        
        let message = self.textView.text ?? ""
        
        if self.defaultString == message {
            self.pop(animationType: .horizontal)
        } else {
            if message.count > 128 {
                Dialog.show(style: .error, title: "入力エラー", message: "128文字以内で入力してください", actions: [DialogAction(title: "OK", action: nil)])
            } else {
                self.completion(message)
                self.pop(animationType: .horizontal)
            }
        }
    }
}
