//
//  CalendarMonthCollectionViewCell.swift
//  Summit
//
//  Created by Leapfrog-Software on 2018/04/30.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class CalendarMonthCollectionViewCell: UICollectionViewCell {
    
    struct CalendarDayInfo {
        let date: Date
        let dateStr: String
        let isToday: Bool
        let isSameMonth: Bool
        let schedules: [ScheduleData]
    }
    
    @IBOutlet private weak var daysCollectionView: UICollectionView!
    
    private var dayInfos = [CalendarDayInfo]()
    private var didSelect: ((CalendarDayInfo) -> ())?
    
    func configure(month: Date, didSelect: @escaping ((CalendarDayInfo)) -> ()) {
        
        self.dayInfos.removeAll()
        
        let dateFormatter = DateFormatter(dateFormat: "d")
        let today = Date()
        
        let firstDay = month.firstMonday()
        let calendar = Calendar(identifier: .gregorian)
        for i in 0..<42 {
            let date = firstDay.add(day: i)
            let schedules = ScheduleRequester.shared.dataList.filter { calendar.isDate($0.date, inSameDayAs: date) }
            let calendarDayInfo = CalendarDayInfo(date: date,
                                                  dateStr: dateFormatter.string(from: date),
                                                  isToday: date.isSameDay(with: today),
                                                  isSameMonth: date.isSameMonth(with: month),
                                                  schedules: schedules)
            self.dayInfos.append(calendarDayInfo)
        }
        self.daysCollectionView.reloadData()
        
        self.didSelect = didSelect
    }
}

extension CalendarMonthCollectionViewCell: UICollectionViewDataSource, UICollectionViewDelegate, UICollectionViewDelegateFlowLayout {
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return self.dayInfos.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "CalendarDayCollectionViewCell", for: indexPath) as! CalendarDayCollectionViewCell
        let dayInfo = self.dayInfos[indexPath.row]
        
        cell.configure(day: dayInfo.dateStr,
                       isToday: dayInfo.isToday,
                       isSameMonth: dayInfo.isSameMonth,
                       schedules: dayInfo.schedules)
        
        return cell
    }
    
    public func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        return CGSize(width: collectionView.frame.size.width / 7,
                      height: 44)
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        self.didSelect?(self.dayInfos[indexPath.row])
    }
}
