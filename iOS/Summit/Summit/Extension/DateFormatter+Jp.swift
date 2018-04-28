//
//  DateFormatter+Jp.swift
//  Summit
//
//  Created by Leapfrog-Software on 2018/04/29.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import Foundation

extension DateFormatter {
    
    convenience init(dateFormat: String) {
        self.init()
        
        self.locale = Locale(identifier: "ja_JP")
        self.calendar = Calendar(identifier: .gregorian)
        self.dateFormat = dateFormat
    }
}
