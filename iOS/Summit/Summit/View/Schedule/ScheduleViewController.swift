//
//  ScheduleViewController.swift
//  Summit
//
//  Created by Leapfrog-Software on 2018/04/29.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class ScheduleViewController: UIViewController {

    @IBOutlet private weak var monthLabel: UILabel!
    @IBOutlet private weak var monthsCollectionView: UICollectionView!
    @IBOutlet private weak var scheduleTableView: UITableView!
    
    private var months = [Date]()
    private var selectedDate = Date()
    private var selectedSchedules = [ScheduleData]()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        for i in 0..<6 {
            self.months.append(Date().add(month: i))
        }
        self.setMonthLabel(index: 0)
    }
    
    private func currentIndex() -> Int {
        return Int(self.monthsCollectionView.contentOffset.x / self.monthsCollectionView.frame.size.width)
    }
    
    private func setMonthLabel(index: Int) {
        self.monthLabel.text = DateFormatter(dateFormat: "yyyy年 M月").string(from: self.months[index])
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

extension ScheduleViewController: UICollectionViewDataSource, UICollectionViewDelegate, UICollectionViewDelegateFlowLayout {
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return self.months.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "CalendarMonthCollectionViewCell", for: indexPath) as! CalendarMonthCollectionViewCell
        cell.configure(month: self.months[indexPath.row], didSelect: { [weak self] dayInfo in
            self?.selectedDate = dayInfo.date
            self?.selectedSchedules = dayInfo.schedules
            self?.scheduleTableView.setContentOffset(.zero, animated: false)
            self?.scheduleTableView.reloadData()
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
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.selectedSchedules.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "ScheduleEventTableViewCell", for: indexPath) as! ScheduleEventTableViewCell
        cell.configure(scheduleData: self.selectedSchedules[indexPath.row])
        return cell
    }
    
    public func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        let scheduleData = self.selectedSchedules[indexPath.row]
        
        let detailViewController = self.viewController(storyboard: "Schedule", identifier: "ScheduleDetailViewController") as! ScheduleDetailViewController
        detailViewController.set(scheduleData: scheduleData)
        self.tabbarViewController()?.stack(viewController: detailViewController, animationType: .horizontal)
    }
}
