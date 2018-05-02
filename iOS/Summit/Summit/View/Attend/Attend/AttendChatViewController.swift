//
//  AttendChatViewController.swift
//  Summit
//
//  Created by Leapfrog-Software on 2018/05/02.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class AttendChatViewController: UIViewController {
    
    struct Const {
        static let chatTopLimit = CGFloat(120)
        static let chatBottomLimit = CGFloat(80)
    }

    @IBOutlet private weak var headerView: UIView!
    
    private var touchOffset: CGPoint?
    
    func set(chatList: [ChatData]) {
        
    }
    
    private func alignFrame() {

        let targetY: CGFloat
        
        if self.view.frame.origin.y < UIScreen.main.bounds.size.height / 2 {
            targetY = Const.chatTopLimit
        } else {
            targetY = UIScreen.main.bounds.size.height - Const.chatBottomLimit
        }
        
        UIView.animate(withDuration: 0.2) {
            self.view.frame = CGRect(x: 0, y: targetY, width: self.view.frame.size.width, height: self.view.frame.size.height)
        }
    }

    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        
        guard let location = touches.first?.location(in: UIApplication.shared.keyWindow) else {
            return
        }
        let headerFrame = self.headerView.absoluteFrame()
        if headerFrame.contains(location) {
            self.touchOffset = CGPoint(x: location.x - headerFrame.origin.x,
                                       y: location.y - headerFrame.origin.y)
        } else {
            self.touchOffset = nil
        }
    }
    
    override func touchesMoved(_ touches: Set<UITouch>, with event: UIEvent?) {
        
        guard let touchOffset = self.touchOffset,
            let location = touches.first?.location(in: UIApplication.shared.keyWindow) else {
            return
        }
        
        let offsetY = location.y - touchOffset.y

        if offsetY < Const.chatTopLimit || UIScreen.main.bounds.size.height - offsetY < Const.chatBottomLimit {
            return
        }
        self.view.frame = CGRect(x: 0,
                                 y: offsetY,
                                 width: self.view.frame.size.width,
                                 height: self.view.frame.size.height)
    }
    
    override func touchesEnded(_ touches: Set<UITouch>, with event: UIEvent?) {
        self.alignFrame()
        self.touchOffset = nil
    }
    
    override func touchesCancelled(_ touches: Set<UITouch>, with event: UIEvent?) {
        self.alignFrame()
        self.touchOffset = nil
    }
}
