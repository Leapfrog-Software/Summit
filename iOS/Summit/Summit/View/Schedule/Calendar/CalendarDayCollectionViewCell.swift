//
//  CalendarDayCollectionViewCell.swift
//  Summit
//
//  Created by Leapfrog-Software on 2018/04/30.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class CalendarDayCollectionViewCell: UICollectionViewCell {
    
    struct Const {
        static let typeViewWidth = CGFloat(8)
    }
    
    @IBOutlet private weak var dayLabel: UILabel!
    @IBOutlet private weak var selectedMarkView: UIView!
    @IBOutlet private weak var plan1BaseView: UIView!
    @IBOutlet private weak var plan2BaseView: UIView!
    @IBOutlet private weak var plan3BaseView: UIView!
    @IBOutlet private weak var plan1BaseWidthConstraint: NSLayoutConstraint!
    @IBOutlet private weak var plan2BaseWidthConstraint: NSLayoutConstraint!
    @IBOutlet private weak var plan3BaseWidthConstraint: NSLayoutConstraint!
    @IBOutlet private weak var plan1View: UIView!
    @IBOutlet private weak var plan2View: UIView!
    @IBOutlet private weak var plan3View: UIView!
    
    func configure(day: String, isToday: Bool, isSameMonth: Bool, schedules: [ScheduleData]) {
        
        self.dayLabel.text = day
        
        if isSameMonth {
            if isToday {
                self.dayLabel.textColor = .white
                self.selectedMarkView.isHidden = false
            } else {
                self.dayLabel.textColor = .calendarActiveDate
                self.selectedMarkView.isHidden = true
            }
        } else {
            self.dayLabel.textColor = .calendarPassiveDate
            self.selectedMarkView.isHidden = true
        }
        
        if schedules.count >= 1 {
            self.plan1View.backgroundColor = schedules[0].type.viewColor()
            self.plan1BaseView.isHidden = false
            self.plan1BaseWidthConstraint.constant = Const.typeViewWidth
        } else {
            self.plan1BaseView.isHidden = true
            self.plan1BaseWidthConstraint.constant = 0
        }
        if schedules.count >= 2 {
            self.plan2View.backgroundColor = schedules[1].type.viewColor()
            self.plan2BaseView.isHidden = false
            self.plan2BaseWidthConstraint.constant = Const.typeViewWidth
        } else {
            self.plan2BaseView.isHidden = true
            self.plan2BaseWidthConstraint.constant = 0
        }
        if schedules.count >= 3 {
            self.plan3View.backgroundColor = schedules[2].type.viewColor()
            self.plan3BaseView.isHidden = false
            self.plan3BaseWidthConstraint.constant = Const.typeViewWidth
        } else {
            self.plan3BaseView.isHidden = true
            self.plan3BaseWidthConstraint.constant = 0
        }
    }
}
