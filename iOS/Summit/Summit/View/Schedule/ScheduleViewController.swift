//
//  ScheduleViewController.swift
//  Summit
//
//  Created by Leapfrog-Software on 2018/04/29.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class ScheduleViewController: UIViewController {
    
    struct Const {
        static let scheduleTableCellHeight = CGFloat(116)
    }

    @IBOutlet private weak var nextPlanHeightConstraint: NSLayoutConstraint!
    @IBOutlet private weak var nextPlanBaseView: UIView!
    @IBOutlet private weak var nextPlanShadowView: UIView!
    @IBOutlet private weak var nextPlanScheduleLabel: UILabel!
    @IBOutlet private weak var nextPlanImageView: UIImageView!
    @IBOutlet private weak var nextPlantitleLabel: UILabel!
    @IBOutlet private weak var nextPlanDateLabel: UILabel!
    @IBOutlet private weak var nextPlanArrowImageView: UIImageView!
    
    @IBOutlet private weak var monthLabel: UILabel!
    @IBOutlet private weak var monthsCollectionView: UICollectionView!
    @IBOutlet private weak var scheduleDateLabel: UILabel!
    @IBOutlet private weak var scheduleTableView: UITableView!
    @IBOutlet private weak var scheduleTableViewHeightConstraint: NSLayoutConstraint!
    @IBOutlet private weak var noDataLabel: UILabel!
    
    private var months = [Date]()
    private var selectedDate = Date()
    private var selectedSchedules = [ScheduleData]()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        for i in 0..<6 {
            self.months.append(Date().add(month: i))
        }
        self.setMonthLabel(index: 0)
        self.setScheduleDateLabel()
        
        let initialSchedules = ScheduleRequester.shared.dataList.filter { $0.date.isSameDay(with: Date()) }
        self.resetSchedules(schedules: initialSchedules)
        
        self.nextPlanShadowView.layer.masksToBounds = false
        self.nextPlanShadowView.layer.shadowOffset = CGSize(width: 0, height: 8)
        self.nextPlanShadowView.layer.shadowOpacity = 0.4
        self.nextPlanShadowView.layer.shadowColor = UIColor(white: 0.8, alpha: 1.0).cgColor
        self.nextPlanShadowView.layer.shadowRadius = 4

        self.timerProc()
        Timer.scheduledTimer(withTimeInterval: 10, repeats: true, block: { _ in
            self.timerProc()
        })
    }
    
    private func currentIndex() -> Int {
        return Int(self.monthsCollectionView.contentOffset.x / self.monthsCollectionView.frame.size.width)
    }
    
    private func setMonthLabel(index: Int) {
        self.monthLabel.text = DateFormatter(dateFormat: "yyyy年 M月").string(from: self.months[index])
    }
    
    private func setScheduleDateLabel() {
        self.scheduleDateLabel.text = DateFormatter(dateFormat: "M月d日の予定").string(from: self.selectedDate)
    }
    
    private func resetSchedules(schedules: [ScheduleData]) {
        
        self.selectedSchedules = schedules
        self.scheduleTableView.setContentOffset(.zero, animated: false)
        self.monthsCollectionView.reloadData()
        self.scheduleTableView.reloadData()
        if schedules.count > 0 {
            self.scheduleTableViewHeightConstraint.constant = CGFloat(schedules.count) * Const.scheduleTableCellHeight
            self.scheduleTableView.isHidden = false
            self.noDataLabel.isHidden = true
        } else {
            self.scheduleTableViewHeightConstraint.constant = 160
            self.scheduleTableView.isHidden = true
            self.noDataLabel.isHidden = false
        }
    }
    
    @IBAction func onTapPreviousMonth(_ sender: Any) {
        
        let index = self.currentIndex()
        if index > 0 {
            self.monthsCollectionView.selectItem(at: IndexPath(row: index - 1, section: 0), animated: true, scrollPosition: .centeredHorizontally)
            self.setMonthLabel(index: index - 1)
        }
    }
    
    @IBAction func onTapNextMonth(_ sender: Any) {
        
        let index = self.currentIndex()
        if index < self.months.count - 1 {
            self.monthsCollectionView.selectItem(at: IndexPath(row: index + 1, section: 0), animated: true, scrollPosition: .centeredHorizontally)
            self.setMonthLabel(index: index + 1)
        }
    }
}

extension ScheduleViewController {
    
    private func searchNextSchedule() -> ScheduleData? {
        
        guard let myUserData = UserRequester.shared.myUserData() else {
            return nil
        }
        let today = Date()
        let reservedSchedules = myUserData.reserves.compactMap { ScheduleRequester.shared.query(id: $0) }
        let futureSchedules = reservedSchedules.filter { $0.date + $0.timeLength > today }
        let sortedSchedules = futureSchedules.sorted { $0.date < $1.date }
        return sortedSchedules.first
    }

    private func timerProc() {
        
        if let nextSchedule = self.searchNextSchedule() {
            self.nextPlanHeightConstraint.constant = 122
            self.nextPlanBaseView.isHidden = false
            self.nextPlanImageView.image = nil
            ImageStorage.shared.fetch(url: Constants.ScheduleImageDirectory + nextSchedule.id, imageView: self.nextPlanImageView)
            
            let today = Date()
            
            self.nextPlantitleLabel.text = nextSchedule.title
            if nextSchedule.date.isSameDay(with: today) {
                self.nextPlanDateLabel.text = DateFormatter(dateFormat: "H:mm〜").string(from: nextSchedule.date)
            } else if nextSchedule.date.isSameYear(with: today) {
                self.nextPlanDateLabel.text = DateFormatter(dateFormat: "yyyy年MM月dd日 H:mm〜").string(from: nextSchedule.date)
            }
            
            let timeInterval = nextSchedule.date.timeIntervalSince(today)
            if timeInterval <= 0 && timeInterval >= -nextSchedule.timeLength {
                self.nextPlanScheduleLabel.text = "開催中"
                self.nextPlanArrowImageView.isHidden = true
            } else if timeInterval < 60 * 60 {
                let remainMinute = Int(timeInterval / 60) + 1
                self.nextPlanScheduleLabel.text = "\(remainMinute)分後に開始"
                self.nextPlanArrowImageView.isHidden = false
            } else {
                self.nextPlanScheduleLabel.text = "直近の予定"
                self.nextPlanArrowImageView.isHidden = true
            }
        } else {
            self.nextPlanHeightConstraint.constant = 0
            self.nextPlanBaseView.isHidden = true
        }
    }
    
    @IBAction func onTapNextSchedule(_ sender: Any) {
        
        guard let nextSchedule = self.searchNextSchedule() else {
            return
        }
        let timeInterval = nextSchedule.date.timeIntervalSince(Date())
        if timeInterval >= 60 * 60 {
            return
        }
        let attendPrepare = self.viewController(storyboard: "Attend", identifier: "AttendPrepareViewController") as! AttendPrepareViewController
        attendPrepare.set(scheduleData: nextSchedule)
        self.tabbarViewController()?.stack(viewController: attendPrepare, animationType: .vertical)
    }
}

extension ScheduleViewController: UICollectionViewDataSource, UICollectionViewDelegate, UICollectionViewDelegateFlowLayout {
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return self.months.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "CalendarMonthCollectionViewCell", for: indexPath) as! CalendarMonthCollectionViewCell
        cell.configure(month: self.months[indexPath.row], selectedDate: self.selectedDate, didSelect: { [weak self] dayInfo in
            self?.selectedDate = dayInfo.date
            self?.resetSchedules(schedules: dayInfo.schedules)
            self?.setScheduleDateLabel()
        })
        return cell
    }
    
    public func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        return collectionView.frame.size
    }
    
    func scrollViewDidEndDecelerating(_ scrollView: UIScrollView) {
        self.setMonthLabel(index: self.currentIndex())
    }
}

extension ScheduleViewController: UITableViewDataSource, UITableViewDelegate {
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return Const.scheduleTableCellHeight
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.selectedSchedules.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "ScheduleEventTableViewCell", for: indexPath) as! ScheduleEventTableViewCell
        cell.configure(scheduleData: self.selectedSchedules[indexPath.row])
        return cell
    }
    
    public func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        tableView.deselectRow(at: indexPath, animated: true)
        
        let scheduleData = self.selectedSchedules[indexPath.row]
        
        let detailViewController = self.viewController(storyboard: "Schedule", identifier: "ScheduleDetailViewController") as! ScheduleDetailViewController
        detailViewController.set(scheduleData: scheduleData)
        self.tabbarViewController()?.stack(viewController: detailViewController, animationType: .horizontal)
    }
}
