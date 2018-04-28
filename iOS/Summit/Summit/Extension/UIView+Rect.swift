//
//  UIView+Rect.swift
//  Summit
//
//  Created by Leapfrog-Software on 2018/04/29.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

extension UIView {
    
    func absoluteFrame() -> CGRect {
        
        return self.convert(self.bounds, to: UIApplication.shared.keyWindow)
    }
}
