//
//  SplashViewController.swift
//  Summit
//
//  Created by Leapfrog-Software on 2018/04/29.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class SplashViewController: UIViewController {
    
    enum ResultKey: String {
        case createUser = "CreateUser"
        case schedule = "Schedule"
        case user = "User"
        case message = "Message"
        case review = "Review"
    }
    
    private var results = Dictionary<ResultKey, Bool>()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        if SaveData.shared.userId.count > 0 {
            self.results[.createUser] = true
        }
        self.refresh()
    }
    
    private func refresh() {
        
        if self.results[.createUser] != true {
            AccountRequester.createUser(completion: { [weak self] result, userId in
                if result, let uid = userId {
                    let saveData = SaveData.shared
                    saveData.userId = uid
                    saveData.save()
                    
                    self?.results[.createUser] = true
                } else {
                    self?.results[.createUser] = false
                }
                self?.refreshMasterData()
            })
        } else {
            self.refreshMasterData()
        }
    }
    
    private func refreshMasterData() {
        
        if self.results[.schedule] != true {
            ScheduleRequester.shared.fetch(completion: { [weak self] result in
                self?.results[.schedule] = result
                self?.checkResult()
            })
        }
        if self.results[.user] != true {
            UserRequester.shared.fetch(completion: { [weak self] result in
                self?.results[.user] = result
                self?.checkResult()
            })
        }
        if self.results[.message] != true {
            MessageRequester.shared.fetch(completion: { [weak self] result in
                self?.results[.message] = result
                self?.checkResult()
            })
        }
        if self.results[.review] != true {
            ReviewRequester.shared.fetch(completion: { [weak self] result, _ in
                self?.results[.review] = result
                self?.checkResult()
            })
        }
    }
    
    private func checkResult() {
        
        let keys: [ResultKey] = [.createUser, .schedule, .user, .message, .review]
        let results = keys.map { self.results[$0] }
        if results.contains(where: { $0 == nil }) {
            return
        }
        if results.contains(where: { $0 == false }) {
            self.showError()
        } else {
            self.stackTabbar()
        }
    }
    
    private func showError() {
        Dialog.show(style: .error, title: "エラー", message: "通信に失敗しました", actions: [DialogAction(title: "OK", action: {
            self.refresh()
        })])
    }
    
    private func stackTabbar() {
        
        let blackView = UIView(frame: CGRect(origin: .zero, size: self.view.frame.size))
        blackView.backgroundColor = .black
        blackView.alpha = 0.0
        self.view.addSubview(blackView)
        
        UIView.animate(withDuration: 0.2, animations: {
            blackView.alpha = 1.0
        }, completion: { [weak self] _ in
            if let tabbarViewController = self?.viewController(storyboard: "Main", identifier: "TabbarViewController") {
                self?.stack(viewController: tabbarViewController, animationType: .none)
            }
            self?.view.bringSubview(toFront: blackView)
            
            UIView.animate(withDuration: 0.2, animations: {
                blackView.alpha = 0.0
            }, completion: { _ in
                blackView.removeFromSuperview()
            })
        })
    }
}
