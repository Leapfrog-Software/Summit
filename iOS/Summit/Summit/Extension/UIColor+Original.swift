//
//  UIColor+Original.swift
//  Summit
//
//  Created by Leapfrog-Software on 2018/04/29.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

extension UIColor {
    
    class var mainBlue: UIColor {
        return UIColor(red: 30 / 255, green: 72 / 255, blue: 158 / 255, alpha: 1.0)
    }
    
    class var tabSelected: UIColor {
        return .mainBlue
    }
    
    class var tabUnselected: UIColor {
        return UIColor(red: 239 / 255, green: 239 / 255, blue: 244 / 255, alpha: 1.0)
    }
    
    class var calendarActiveDate: UIColor {
        return UIColor(red: 34 / 255, green: 34 / 255, blue: 34 / 255, alpha: 1.0)
    }
    
    class var calendarPassiveDate: UIColor {
        return UIColor(red: 239 / 255, green: 239 / 255, blue: 244 / 255, alpha: 1.0)
    }
    
    class var scheduleTypeSeminar: UIColor {
        return UIColor(red: 91 / 255, green: 237 / 255, blue: 205 / 255, alpha: 1.0)
    }
    
    class var scheduleTypeMeeting: UIColor {
        return UIColor(red: 36 / 255, green: 87 / 255, blue: 245 / 255, alpha: 1.0)
    }
    
    class var scheduleTypeParty: UIColor {
        return UIColor(red: 255 / 255, green: 0 / 255, blue: 104 / 255, alpha: 1.0)
    }
    
    class var cardIndexActive: UIColor {
        return UIColor(red: 33 / 255, green: 33 / 255, blue: 34 / 255, alpha: 1.0)
    }
    
    class var cardIndexPassive: UIColor {
        return UIColor(red: 171 / 255, green: 180 / 255, blue: 189 / 255, alpha: 1.0)
    }
    
    class var settingNoName: UIColor {
        return UIColor(red: 171 / 255, green: 171 / 255, blue: 171 / 255, alpha: 1.0)
    }
    
    class var placeholder: UIColor {
        return UIColor(red: 146 / 255, green: 146 / 255, blue: 146 / 255, alpha: 1)
    }
    
    class var dialogActionSuccess: UIColor {
        return UIColor(red: 123 / 255, green: 209 / 255, blue: 249 / 255, alpha: 1)
    }
    
    class var dialogActionError: UIColor {
        return UIColor(red: 230 / 255, green: 73 / 255, blue: 66 / 255, alpha: 1)
    }
    
    class var matchMessageSendButtonActive: UIColor {
        return UIColor(red: 36 / 255, green: 87 / 255, blue: 245 / 255, alpha: 1)
    }
    
    class var matchMessageSendButtonInactive: UIColor {
        return UIColor(red: 200 / 255, green: 171 / 255, blue: 171 / 255, alpha: 1)
    }
}
