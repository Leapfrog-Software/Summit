//
//  ApiManager.swift
//  Summit
//
//  Created by Leapfrog-Software on 2018/04/29.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import Foundation

class ApiManager {
    
    class func post(params: [String: String]?, completion: @escaping ((Bool, Any?) -> ())) {
        
        HttpManager.post(url: Constants.ServerApiUrl, params: params) { result, data in
            if result, let data = data {
                do {
                    if let json = try JSONSerialization.jsonObject(with: data, options: JSONSerialization.ReadingOptions.allowFragments) as? Dictionary<String, Any> {
                        if let result = json["result"] as? String, result == "0" {
                            completion(true, json["data"])
                            return
                        }
                    }
                } catch {}
            }
            completion(false, nil)
        }
    }
}
