//
//  ReportRequester.swift
//  Summit
//
//  Created by Leapfrog-Software on 2018/05/31.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import Foundation

class ReportRequester {
    
    class func post(userId: String, targetId: String, reason: String, completion: @escaping ((Bool) -> ())) {
        
        let params = [
            "command": "report",
            "userId": userId,
            "targetId": targetId,
            "reason": reason,
            ]
        ApiManager.post(params: params) { (result, data) in
            completion(result)
        }
    }
}
