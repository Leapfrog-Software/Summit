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
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.initContents()
    }
    
    private func initContents() {
        
        guard let myUserData = UserRequester.shared.myUserData() else {
            return
        }
        
        ImageStorage.shared.fetch(url: Constants.UserImageDirectory + myUserData.userId, imageView: self.faceImageView)

        self.messageLabel.set(text: myUserData.message, lineHeight: 16)
        self.nameLabel.text = myUserData.nameLast + " " + myUserData.nameFirst
        self.kanaLabel.text = myUserData.kanaLast + " " + myUserData.kanaFirst
        self.ageLabel.text = myUserData.age.toText()
        self.genderLabel.text = myUserData.gender.toText()
        self.companyLabel.text = myUserData.company
        self.positionLabel.text = myUserData.position
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
        
    }
    
    @IBAction func onTapName(_ sender: Any) {
        
    }
    
    @IBAction func onTapKana(_ sender: Any) {
        
    }
    
    @IBAction func onTapAge(_ sender: Any) {
        
        guard let myUserData = UserRequester.shared.myUserData() else {
            return
        }
        let dataArray = AgeType.allValue().map { $0.toText() }
        let defaultIndex = AgeType.allValue().index(of: myUserData.age)
        let picker = self.viewController(storyboard: "Common", identifier: "PickerViewController") as! PickerViewController
        picker.set(title: "年代", dataArray: dataArray, defaultIndex: defaultIndex, completion: { index in
            
        })
        self.tabbarViewController()?.stack(viewController: picker, animationType: .none)
    }
    
    @IBAction func onTapGender(_ sender: Any) {
        
    }
    
    @IBAction func onTapCompany(_ sender: Any) {
        
    }
    
    @IBAction func onTapPosition(_ sender: Any) {
        
    }
    
    @IBAction func onTapBack(_ sender: Any) {
        self.pop(animationType: .horizontal)
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
