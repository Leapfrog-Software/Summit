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
        
        self.timeLength = TimeInterval(30 * 60)     // TODO
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
