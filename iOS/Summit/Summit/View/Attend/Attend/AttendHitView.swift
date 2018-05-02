//
//  AttendHitView.swift
//  Summit
//
//  Created by Leapfrog-Software on 2018/05/02.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

protocol AttendViewDelegate {
    func hitTest(_ point: CGPoint, with event: UIEvent?) -> UIView?
}

class AttendHitView: UIView {

    var delegate: AttendViewDelegate?
    
    override func hitTest(_ point: CGPoint, with event: UIEvent?) -> UIView? {
        return delegate?.hitTest(point, with: event)
    }
}
