//
//  SaveData.swift
//  Summit
//
//  Created by Leapfrog-Software on 2018/04/29.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import Foundation

class SaveData {
    
    static let shared = SaveData()
    
    var userId = ""
    var sentFirstMessageScheduleIds = [String]()
    
    init() {
        let userDefaults = UserDefaults()
        self.userId = userDefaults.string(forKey: Constants.UserDefaultsKey.UserId) ?? ""
        self.sentFirstMessageScheduleIds = userDefaults.array(forKey: Constants.UserDefaultsKey.SentFirstMessageScheduleIds) as? [String] ?? []
    }
    
    func save() {
        
        let userDefaults = UserDefaults()
        
        userDefaults.set(self.userId, forKey: Constants.UserDefaultsKey.UserId)
        userDefaults.set(self.sentFirstMessageScheduleIds, forKey: Constants.UserDefaultsKey.SentFirstMessageScheduleIds)
        
        userDefaults.synchronize()
    }
}
