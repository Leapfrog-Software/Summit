//
//  AttendTableView.swift
//  Summit
//
//  Created by Leapfrog-Software on 2018/05/02.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class AttendTableView: UIView {

    func set(userIds: [String]) {
        
        for i in 0..<8 {
            let userView = self.subviews.compactMap { $0 as? AttendTableUserView }.filter { $0.tag == i }.first!
            if userIds.count > i {
                userView.isHidden = false
                userView.set(userId: userIds[i])
            } else {
                userView.isHidden = true
            }            
        }
    }
    
    override func didMoveToSuperview() {
        super.didMoveToSuperview()
        
        self.subviews.compactMap { $0 as? AttendTableUserView }.forEach { $0.removeFromSuperview() }
        
        for i in 0..<8 {
            let userView = UINib(nibName: "AttendTableUserView", bundle: nil).instantiate(withOwner: nil, options: nil).first as! AttendTableUserView
            userView.isHidden = true
            userView.tag = i
            userView.frame = self.userViewFrame(index: i)
            self.addSubview(userView)
        }
    }
    
    private func userViewFrame(index: Int) -> CGRect {

        let degrees: [Double] = [-90, 90, 0, 180, -45, 135, 225, 45]
        let radians: [Double] = degrees.map { $0 / 180 * Double.pi }
        
        let w = self.frame.size.width * 3 / 10
        let h = self.frame.size.height / 4 + 26
        
        let r = self.frame.size.width / 3
        let x = self.frame.size.width / 2 + r * CGFloat(cos(radians[index])) - w / 2
        let y = self.frame.size.height / 2 + r * CGFloat(sin(radians[index])) - w / 2
        
        return CGRect(x: x, y: y, width: w, height: h)
    }
}
