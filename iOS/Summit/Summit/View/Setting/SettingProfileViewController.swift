//
//  SettingProfileViewController.swift
//  Summit
//
//  Created by Leapfrog-Software on 2018/05/01.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class SettingProfileViewController: UIViewController {
    
    @IBOutlet private weak var faceImageView: UIImageView!
    @IBOutlet private weak var messageLabel: UILabel!
    @IBOutlet private weak var nameLabel: UILabel!
    @IBOutlet private weak var kanaLabel: UILabel!
    @IBOutlet private weak var ageLabel: UILabel!
    @IBOutlet private weak var genderLabel: UILabel!
    @IBOutlet private weak var companyLabel: UILabel!
    @IBOutlet private weak var positionLabel: UILabel!
    
    private var selectedImage: UIImage?
    private var editedMessage: String?
    private var editedNameLast: String?
    private var editedNameFirst: String?
    private var editedKanaLast: String?
    private var editedKanaFirst: String?
    private var selectedAge: AgeType?
    private var selectedGender: GenderType?
    private var editedCompany: String?
    private var editedPosition: String?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.initContents()
    }
    
    private func initContents() {
        
        guard let myUserData = UserRequester.shared.myUserData() else {
            return
        }
        
        ImageStorage.shared.fetch(url: Constants.UserImageDirectory + myUserData.userId, imageView: self.faceImageView)

        self.setMessage(myUserData.message)
        self.setName(nameLast: myUserData.nameLast, nameFirst: myUserData.nameFirst)
        self.setKana(kanaLast: myUserData.kanaLast, kanaFirst: myUserData.kanaFirst)
        self.setAge(myUserData.age)
        self.setGender(myUserData.gender)
        self.setCompany(myUserData.company)
        self.setPosition(myUserData.position)
    }
    
    private func setMessage(_ text: String) {
        if text.isEmpty {
            self.messageLabel.text = "(未設定)"
            self.messageLabel.textColor = UIColor.profileInactive
        } else {
            self.messageLabel.set(text: text, lineHeight: 16)
            self.messageLabel.textColor = UIColor.profileActive
        }
    }
    
    private func setName(nameLast: String, nameFirst: String) {
        
        if nameLast.isEmpty && nameFirst.isEmpty {
            self.nameLabel.text = "(未設定)"
            self.nameLabel.textColor = UIColor.profileInactive
        } else {
            self.nameLabel.text = nameLast + " " + nameFirst
            self.nameLabel.textColor = UIColor.profileActive
        }
    }
    
    private func setKana(kanaLast: String, kanaFirst: String) {
        
        if kanaLast.isEmpty && kanaFirst.isEmpty {
            self.kanaLabel.text = "(未設定)"
            self.kanaLabel.textColor = UIColor.profileInactive
        } else {
            self.kanaLabel.text = kanaLast + " " + kanaFirst
            self.kanaLabel.textColor = UIColor.profileActive
        }
    }
    
    private func setAge(_ age: AgeType) {
        self.ageLabel.text = age.toText()
        self.ageLabel.textColor = (age == .unknown) ? UIColor.profileInactive : UIColor.profileActive
    }
    
    private func setGender(_ gender: GenderType) {
        self.genderLabel.text = gender.toText()
        self.genderLabel.textColor = (gender == .unknown) ? UIColor.profileInactive : UIColor.profileActive
    }
    
    private func setCompany(_ text: String) {
        if text.isEmpty {
            self.companyLabel.text = "(未設定)"
            self.companyLabel.textColor = UIColor.profileInactive
        } else {
            self.companyLabel.text = text
            self.companyLabel.textColor = UIColor.profileActive
        }
    }
    
    private func setPosition(_ text: String) {
        if text.isEmpty {
            self.positionLabel.text = "(未設定)"
            self.positionLabel.textColor = UIColor.profileInactive
        } else {
            self.positionLabel.text = text
            self.positionLabel.textColor = UIColor.profileActive
        }
    }
    
    @IBAction func onTapFace(_ sender: Any) {
        
        if !UIImagePickerController.isSourceTypeAvailable(.photoLibrary) {
            return
        }
        let imagePicker = UIImagePickerController()
        imagePicker.sourceType = .photoLibrary
        imagePicker.delegate = self
        self.present(imagePicker, animated: true)
    }
    
    @IBAction func onTapMessage(_ sender: Any) {
        let messageEdit = self.viewController(storyboard: "Setting", identifier: "SettingProfileMessageViewController") as! SettingProfileMessageViewController
        messageEdit.set(defaultString: UserRequester.shared.myUserData()?.message ?? "", completion: { [weak self] message in
            self?.setMessage(message)
            self?.editedMessage = message
        })
        self.tabbarViewController()?.stack(viewController: messageEdit, animationType: .horizontal)
    }
    
    @IBAction func onTapName(_ sender: Any) {
        
        guard let myUserData = UserRequester.shared.myUserData() else {
            return
        }
        let nameEdit = self.viewController(storyboard: "Setting", identifier: "SettingProfileNameViewController") as! SettingProfileNameViewController
        nameEdit.set(defaultLast: myUserData.nameLast, defaultFirst: myUserData.nameFirst, isKana: false, completion: { [weak self] nameLast, nameFirst in
            self?.editedNameLast = nameLast
            self?.editedNameFirst = nameFirst
            self?.setName(nameLast: nameLast, nameFirst: nameFirst)
        })
        self.tabbarViewController()?.stack(viewController: nameEdit, animationType: .horizontal)
    }
    
    @IBAction func onTapKana(_ sender: Any) {
        
        guard let myUserData = UserRequester.shared.myUserData() else {
            return
        }
        let nameEdit = self.viewController(storyboard: "Setting", identifier: "SettingProfileNameViewController") as! SettingProfileNameViewController
        nameEdit.set(defaultLast: myUserData.kanaLast, defaultFirst: myUserData.kanaFirst, isKana: true, completion: { [weak self] kanaLast, kanaFirst in
            self?.editedKanaLast = kanaLast
            self?.editedKanaFirst = kanaFirst
            self?.setKana(kanaLast: kanaLast, kanaFirst: kanaFirst)
        })
        self.tabbarViewController()?.stack(viewController: nameEdit, animationType: .horizontal)
    }
    
    @IBAction func onTapAge(_ sender: Any) {
        
        guard let myUserData = UserRequester.shared.myUserData() else {
            return
        }
        let dataArray = AgeType.allValue().map { $0.toText() }
        let defaultIndex = AgeType.allValue().index(of: myUserData.age)
        let picker = self.viewController(storyboard: "Common", identifier: "PickerViewController") as! PickerViewController
        picker.set(title: "年代", dataArray: dataArray, defaultIndex: defaultIndex, completion: { [weak self] index in
            let age = AgeType.allValue()[index]
            self?.selectedAge = age
            self?.setAge(age)
        })
        self.tabbarViewController()?.stack(viewController: picker, animationType: .none)
    }
    
    @IBAction func onTapGender(_ sender: Any) {
        
        guard let myUserData = UserRequester.shared.myUserData() else {
            return
        }
        let dataArray = GenderType.allValue().map { $0.toText() }
        let defaultIndex = GenderType.allValue().index(of: myUserData.gender)
        let picker = self.viewController(storyboard: "Common", identifier: "PickerViewController") as! PickerViewController
        picker.set(title: "性別", dataArray: dataArray, defaultIndex: defaultIndex, completion: { [weak self] index in
            let gender = GenderType.allValue()[index]
            self?.selectedGender = gender
            self?.setGender(gender)
        })
        self.tabbarViewController()?.stack(viewController: picker, animationType: .none)
    }
    
    @IBAction func onTapCompany(_ sender: Any) {
        
        let textEdit = self.viewController(storyboard: "Setting", identifier: "SettingProfileTextViewController") as! SettingProfileTextViewController
        
        textEdit.set(title: "会社・組織名", defaultString: UserRequester.shared.myUserData()?.company ?? "", placeholder: "会社・組織名", completion: { [weak self] company in
            self?.editedCompany = company
            self?.setCompany(company)
        })
        self.tabbarViewController()?.stack(viewController: textEdit, animationType: .horizontal)
    }
    
    @IBAction func onTapPosition(_ sender: Any) {
        
        let textEdit = self.viewController(storyboard: "Setting", identifier: "SettingProfileTextViewController") as! SettingProfileTextViewController
        textEdit.set(title: "役職・職種", defaultString: UserRequester.shared.myUserData()?.position ?? "", placeholder: "役職・職種", completion: { [weak self] position in
            self?.editedPosition = position
            self?.setPosition(position)
        })
        self.tabbarViewController()?.stack(viewController: textEdit, animationType: .horizontal)
    }
    
    @IBAction func onTapBack(_ sender: Any) {

        if let selectedImage = self.selectedImage {
            let params = [
                "command": "uploadUserImage",
                "userId": SaveData.shared.userId
            ]
            ImageUploader.post(url: Constants.ServerApiUrl, image: selectedImage, params: params, completion: { result, _ in
                if result {
                    self.updateUserIfNeeded()
                } else {
                    Dialog.show(style: .error, title: "エラー", message: "通信に失敗しました", actions: [DialogAction(title: "OK", action: nil)])
                }
            })
        } else {
            self.updateUserIfNeeded()
        }
    }
    
    private func updateUserIfNeeded() {

        guard var newUserData = UserRequester.shared.myUserData() else {
            self.pop(animationType: .horizontal)
            return
        }
        
        var needed = false
        
        if let message = self.editedMessage {
            newUserData.message = message
            needed = true
        }
        if let nameLast = self.editedNameLast {
            newUserData.nameLast = nameLast
            needed = true
        }
        if let nameFirst = self.editedNameFirst {
            newUserData.nameFirst = nameFirst
            needed = true
        }
        if let kanaLast = self.editedKanaLast {
            newUserData.kanaLast = kanaLast
            needed = true
        }
        if let kanaFirst = self.editedKanaFirst {
            newUserData.kanaFirst = kanaFirst
            needed = true
        }
        if let age = self.selectedAge {
            newUserData.age = age
            needed = true
        }
        if let gender = self.selectedGender {
            newUserData.gender = gender
            needed = true
        }
        if let company = self.editedCompany {
            newUserData.company = company
            needed = true
        }
        if let position = self.editedPosition {
            newUserData.position = position
            needed = true
        }
        
        if needed {
            Loading.start()
            
            AccountRequester.updateUser(userData: newUserData, completion: { result in
                if result {
                    UserRequester.shared.fetch(completion: { result in
                        Loading.stop()
                        
                        if result {
                            let settingViewController = self.tabbarViewController()?.childViewControllers.compactMap { $0 as? SettingViewController }.first
                            settingViewController?.refreshContents()
                        }
                        self.pop(animationType: .horizontal)
                    })
                } else {
                    Loading.stop()
                    Dialog.show(style: .error, title: "エラー", message: "通信に失敗しました", actions: [DialogAction(title: "OK", action: nil)])
                }
            })
        } else {
            self.pop(animationType: .horizontal)
        }
    }
}

extension SettingProfileViewController: UIImagePickerControllerDelegate, UINavigationControllerDelegate {
    
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [String : Any]) {

        picker.dismiss(animated: true)
        
        guard let image = info[UIImagePickerControllerOriginalImage] as? UIImage else {
            return
        }
        if let cropedImage = image.crop(size: CGSize(width: 240, height: 240)) {
            self.selectedImage = cropedImage
            self.faceImageView.image = cropedImage
        }
    }
}
