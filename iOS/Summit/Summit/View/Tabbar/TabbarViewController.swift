//
//  TabbarViewController.swift
//  Summit
//
//  Created by Leapfrog-Software on 2018/04/29.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class TabbarViewController: UIViewController {

    @IBOutlet private weak var container: UIView!
    @IBOutlet private weak var tab1ImageView: UIImageView!
    @IBOutlet private weak var tab2ImageView: UIImageView!
    @IBOutlet private weak var tab3ImageView: UIImageView!
    @IBOutlet private weak var tab4ImageView: UIImageView!
    @IBOutlet private weak var tab1Label: UILabel!
    @IBOutlet private weak var tab2Label: UILabel!
    @IBOutlet private weak var tab3Label: UILabel!
    @IBOutlet private weak var tab4Label: UILabel!
    
    var scheduleViewController: ScheduleViewController!
    var cardViewController: CardViewController!
    var messageViewController: MessageViewController!
    var settingViewController: SettingViewController!

    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.initContents()
        
        Timer.scheduledTimer(withTimeInterval: 3 * 60, repeats: true, block: { [weak self] _ in
            self?.timerProc()
        })
    }
    
    private func initContents() {
        
        self.scheduleViewController = self.viewController(storyboard: "Schedule", identifier: "ScheduleViewController") as! ScheduleViewController
        self.addContents(self.scheduleViewController, isHidden: false)
        self.messageViewController = self.viewController(storyboard: "Message", identifier: "MessageViewController") as! MessageViewController
        self.addContents(self.messageViewController, isHidden: true)
        self.cardViewController = self.viewController(storyboard: "Card", identifier: "CardViewController") as! CardViewController
        self.addContents(self.cardViewController, isHidden: true)
        self.settingViewController = self.viewController(storyboard: "Setting", identifier: "SettingViewController") as! SettingViewController
        self.addContents(self.settingViewController, isHidden: true)
        
        self.changeContents(index: 0)
    }
    
    private func addContents(_ viewController: UIViewController, isHidden: Bool) {
        
        self.container.addSubview(viewController.view)
        self.addChildViewController(viewController)
        viewController.didMove(toParentViewController: self)
        viewController.view.isHidden = isHidden
        
        viewController.view.translatesAutoresizingMaskIntoConstraints = false
        viewController.view.topAnchor.constraint(equalTo: self.container.topAnchor).isActive = true
        viewController.view.leadingAnchor.constraint(equalTo: self.container.leadingAnchor).isActive = true
        viewController.view.trailingAnchor.constraint(equalTo: self.container.trailingAnchor).isActive = true
        viewController.view.bottomAnchor.constraint(equalTo: self.container.bottomAnchor).isActive = true
    }
    
    func changeContents(index: Int) {
        
        self.scheduleViewController.view.isHidden = (index != 0)
        self.cardViewController.view.isHidden = (index != 1)
        self.messageViewController.view.isHidden = (index != 2)
        self.settingViewController.view.isHidden = (index != 3)
        
        self.tab1ImageView.image = UIImage(named: (index == 0) ? "tab1_on" : "tab1_off")
        self.tab2ImageView.image = UIImage(named: (index == 1) ? "tab2_on" : "tab2_off")
        self.tab3ImageView.image = UIImage(named: (index == 2) ? "tab3_on" : "tab3_off")
        self.tab4ImageView.image = UIImage(named: (index == 3) ? "tab4_on" : "tab4_off")
        
        self.tab1Label.textColor = (index == 0) ? .tabSelected : .tabUnselected
        self.tab2Label.textColor = (index == 1) ? .tabSelected : .tabUnselected
        self.tab3Label.textColor = (index == 2) ? .tabSelected : .tabUnselected
        self.tab4Label.textColor = (index == 3) ? .tabSelected : .tabUnselected
    }
    
    private func timerProc() {
        
        UserRequester.shared.fetch(completion: { _ in
            ScheduleRequester.shared.fetch(completion: { _ in
                MessageRequester.shared.fetch(completion: { _ in
                    self.cardViewController.reload()
                    self.messageViewController.reload()
                })
            })
        })
    }
    
    @IBAction func onTapTab1(_ sender: Any) {
        self.changeContents(index: 0)
    }
    
    @IBAction func onTapTab2(_ sender: Any) {
        self.changeContents(index: 1)
    }
    
    @IBAction func onTapTab3(_ sender: Any) {
        self.changeContents(index: 2)
    }
    
    @IBAction func onTapTab4(_ sender: Any) {
        self.changeContents(index: 3)
    }
}
