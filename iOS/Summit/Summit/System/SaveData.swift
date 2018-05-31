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
    var pushSetting = true
    var blockUserIdList = [String]()
    var didTermsAgree = false
    
    init() {
        let userDefaults = UserDefaults()
        self.userId = userDefaults.string(forKey: Constants.UserDefaultsKey.UserId) ?? ""
        self.sentFirstMessageScheduleIds = userDefaults.array(forKey: Constants.UserDefaultsKey.SentFirstMessageScheduleIds) as? [String] ?? []
        self.pushSetting = userDefaults.bool(forKey: Constants.UserDefaultsKey.pushSetting)
        self.blockUserIdList = userDefaults.array(forKey: Constants.UserDefaultsKey.blockUserIdList) as? [String] ?? []
        self.didTermsAgree = userDefaults.bool(forKey: Constants.UserDefaultsKey.didTermsAgree)
    }
    
    func save() {
        
        let userDefaults = UserDefaults()
        
        userDefaults.set(self.userId, forKey: Constants.UserDefaultsKey.UserId)
        userDefaults.set(self.sentFirstMessageScheduleIds, forKey: Constants.UserDefaultsKey.SentFirstMessageScheduleIds)
        userDefaults.set(self.pushSetting, forKey: Constants.UserDefaultsKey.pushSetting)
        userDefaults.set(self.blockUserIdList, forKey: Constants.UserDefaultsKey.blockUserIdList)
        userDefaults.set(self.didTermsAgree, forKey: Constants.UserDefaultsKey.didTermsAgree)
        userDefaults.synchronize()
    }
}
