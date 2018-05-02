//
//  ScheduleRequester.swift
//  Summit
//
//  Created by Leapfrog-Software on 2018/04/29.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import Foundation
import UIKit

enum ScheduleType: String {
    case party = "0"
    case seminar = "1"
    case meeting = "2"
    
    func viewColor() -> UIColor {
        switch self {
        case .party:
            return .scheduleTypeParty
        case .seminar:
            return .scheduleTypeSeminar
        case .meeting:
            return .scheduleTypeMeeting
        }
    }
}

struct ScheduleData {
    let id: String
    let type: ScheduleType
    let title: String
    let date: Date
    let timeLength: TimeInterval
    let provider: String
    let description: String
    let filter: String
    
    init?(data: Dictionary<String, Any>) {
        
        guard let id = data["id"] as? String else {
            return nil
        }
        self.id = id
        
        guard let typeString = data["type"] as? String, let type = ScheduleType(rawValue: typeString) else {
            return nil
        }
        self.type = type
        
        self.title = data["title"] as? String ?? ""
        
        guard let dateStr = data["datetime"] as? String,
            let date = DateFormatter(dateFormat: "yyyyMMddHHmmss").date(from: dateStr) else {
                return nil
        }
        self.date = date
        
        self.timeLength = Double(data["timeLength"] as? String ?? "") ?? 0
        
        self.provider = data["provider"] as? String ?? ""
        self.description = data["description"] as? String ?? ""
        self.filter = data["filter"] as? String ?? ""
    }
}

class ScheduleRequester {
    
    static let shared = ScheduleRequester()
    
    var dataList = [ScheduleData]()
    
    func fetch(completion: @escaping ((Bool) -> ())) {
        
        let params = ["command": "getSchedule"]
        ApiManager.post(params: params) { [weak self] result, data in
            if result, let schedules = data as? Array<Dictionary<String, Any>> {
                self?.dataList = schedules.compactMap { ScheduleData(data: $0) }
                completion(true)
            } else {
                completion(false)
            }
        }
    }
    
    func query(id: String) -> ScheduleData? {
        return (self.dataList.filter { $0.id == id }).first
    }
}
