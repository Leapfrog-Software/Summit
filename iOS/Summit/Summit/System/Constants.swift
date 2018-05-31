//
//  Constants.swift
//  Summit
//
//  Created by Leapfrog-Software on 2018/04/29.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import Foundation

struct Constants {
    
    static let HttpTimeOutInterval = TimeInterval(10)
//    static let ServerRootUrl = "http://lfg.co.jp/summit/"
    static let ServerRootUrl = "http://localhost/summit/"
    static let ServerApiUrl = Constants.ServerRootUrl + "srv.php"
    static let ScheduleImageDirectory = Constants.ServerRootUrl + "data/image/schedule/"
    static let UserImageDirectory = Constants.ServerRootUrl + "data/image/user/"
    static let StringEncoding = String.Encoding.utf8
    
    struct UserDefaultsKey {
        static let UserId = "UserId"
        static let SentFirstMessageScheduleIds = "SentFirstMessageScheduleIds"
        static let pushSetting = "PushSetting"
        static let blockUserIdList = "BlockUserIdList"
    }
    
    struct WebPageUrl {
        static let Terms = "http://lfg.co.jp/summit/static/terms.html"
        static let Privacypolicy = "http://lfg.co.jp/summit/static/privacypolicy.html"
    }
}
