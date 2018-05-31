//
//  ReviewRequester.swift
//  Summit
//
//  Created by Leapfrog-Software on 2018/05/31.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import Foundation

class ReviewRequester {
    
    static let shared = ReviewRequester()
    
    var inReview = false
    var activateDatetime = Date()
    
    func fetch(completion: @escaping ((Bool, Bool?) -> ())) {
        
        let params = [
            "command": "getReview"
        ]
        ApiManager.post(params: params) { [weak self] result, data in
            if result, let datas = data as? Dictionary<String, Any> {
                if let inReview = datas["inReview"] as? String {
                    self?.inReview = (inReview == "1")
                    completion(true, true)
                    return
                }
            }
            completion(false, nil)
        }
    }
}
