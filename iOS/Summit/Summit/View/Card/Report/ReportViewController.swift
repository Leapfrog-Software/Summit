//
//  ReportViewController.swift
//  Summit
//
//  Created by Leapfrog-Software on 2018/05/31.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class ReportViewController: UIViewController {

    @IBOutlet private weak var reasonTextView: UITextView!
    
    private var userData: UserData!
    
    func set(userData: UserData) {
        self.userData = userData
    }
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        self.view.endEditing(true)
    }
    
    @IBAction func onTapSend() {
        
        self.view.endEditing(true)
        
        guard let reason = self.reasonTextView.text else {
            return
        }
        if reason.count == 0 {
            Dialog.show(style: .error, title: "エラー", message: "通報理由を入力してください", actions: [DialogAction(title: "OK", action: nil)])
            return
        }
        
        ReportRequester.post(userId: SaveData.shared.userId, targetId: self.userData.userId, reason: reason, completion: { result in
            if result {
                Dialog.show(style: .success, title: "確認", message: "通報しました", actions: [DialogAction(title: "OK", action: { [weak self] in
                    self?.pop(animationType: .none)
                })])
            } else {
                Dialog.show(style: .error, title: "エラー", message: "通信に失敗しました", actions: [DialogAction(title: "OK", action: nil)])
            }
        })
    }
    
    @IBAction func onTapCancel(_ sender: Any) {
        self.pop(animationType: .none)
    }
}
