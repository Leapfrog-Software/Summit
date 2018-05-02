//
//  PlaceholderTextField.swift
//  Summit
//
//  Created by Leapfrog-Software on 2018/05/02.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class PlaceholderTextField: UITextField {
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
        if let placeholder = self.placeholder {
            self.attributedPlaceholder = NSAttributedString(string: placeholder, attributes: [NSAttributedStringKey.foregroundColor: UIColor.placeholder])
        }
    }
}

