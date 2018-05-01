//
//  UILabel+Attribute.swift
//  Summit
//
//  Created by Leapfrog-Software on 2018/05/01.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

extension UILabel {
    
    func set(text: String, lineHeight: CGFloat) {
        
        let paragraphStyle = NSMutableParagraphStyle()
        paragraphStyle.minimumLineHeight = lineHeight
        paragraphStyle.maximumLineHeight = lineHeight
        paragraphStyle.alignment = self.textAlignment
        let attributedText = NSMutableAttributedString(string: text)
        attributedText.addAttribute(.paragraphStyle, value: paragraphStyle, range: NSRange(location: 0, length: text.count))
        self.attributedText = attributedText
    }
}
