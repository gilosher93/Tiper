//
//  SettingsViewController.swift
//  Tiper_iOS
//
//  Created by גיל אושר on 23.12.2015.
//  Copyright © 2015 gil osher. All rights reserved.
//

import UIKit

class SettingsViewController: UIViewController, UITextFieldDelegate {
    
    var statusBar: UIView!;
    var btnSave: UIButton!;
    var txtSalaryPerHour: UITextField!;
    var lblSalaryPerHour: UILabel!;
    var navBar: UINavigationBar!;
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning();
    }
    
    override func viewDidLoad() {
        super.viewDidLoad();
        view.backgroundColor = UIColor(netHex: 0x2979FF);
        
        statusBar = UIView(frame: CGRect(x: 0, y: 0, width: view.frame.width, height: UIApplication.sharedApplication().statusBarFrame.height));
        statusBar.backgroundColor = UIColor(netHex: 0x1F2839);
        view.addSubview(statusBar);
        
        view.backgroundColor = UIColor(netHex: 0x90CAF9);
        navBar = UINavigationBar(frame: CGRect(x: 0, y: UIApplication.sharedApplication().statusBarFrame.height, width: view.frame.width, height: 40))
        let navItem = UINavigationItem(title: "הגדרות");
        navItem.leftBarButtonItem = UIBarButtonItem(title: "חזור", style: UIBarButtonItemStyle.Done, target: self, action: "backToHome");
        navBar.items = [navItem];
        view.addSubview(navBar);
        
        txtSalaryPerHour = UITextField(frame: CGRect(x: 15, y: navBar.frame.maxY + 20, width: 100, height: 40))
        txtSalaryPerHour.delegate = self;
        txtSalaryPerHour.borderStyle = .RoundedRect;
        txtSalaryPerHour.alpha = 0.5;
        txtSalaryPerHour.textAlignment = .Center;
        txtSalaryPerHour.keyboardType = .DecimalPad;
        addDoneButtonToKeyboard();
        view.addSubview(txtSalaryPerHour);
        
        lblSalaryPerHour = UILabel(frame: CGRect(x: view.frame.width - 100 - 15, y: navBar.frame.maxY + 20, width: 100, height: 40))
        lblSalaryPerHour.textAlignment = .Right;
        lblSalaryPerHour.font = UIFont.boldSystemFontOfSize(18);
        lblSalaryPerHour.text = "שכר לשעה";
        view.addSubview(lblSalaryPerHour);
        
        btnSave = UIButton(type: UIButtonType.System);
        btnSave.frame = CGRect(x: 0, y: view.frame.height - 50, width: view.frame.width, height: 50);
        btnSave.setTitle("שמור שינויים", forState: UIControlState.Normal);
        btnSave.backgroundColor = UIColor(netHex: 0xFFC107);
        btnSave.tintColor = UIColor.blackColor();
        btnSave.addTarget(self, action: "saveSettings:", forControlEvents: UIControlEvents.TouchUpInside)
        view.addSubview(btnSave);
    }
    
    func saveSettings(sender: UIButton){
        /* save settings to local file */
        dismissViewControllerAnimated(true, completion: nil);
    }
    
    func backToHome(){
        dismissViewControllerAnimated(true, completion: nil);
    }
    
    func addDoneButtonToKeyboard(){
        let doneToolbar: UIToolbar = UIToolbar(frame: CGRectMake(0, 0, view.frame.width, 30));
        doneToolbar.barStyle = .Default;
        
        let flexSpace = UIBarButtonItem(barButtonSystemItem: UIBarButtonSystemItem.FlexibleSpace, target: nil, action: nil);
        let done: UIBarButtonItem = UIBarButtonItem(title: "Done", style: UIBarButtonItemStyle.Done, target: self, action: Selector("keyboardDoneButtonPressed"));
        
        var items = [UIBarButtonItem]();
        items.append(flexSpace)
        items.append(done)
        
        doneToolbar.items = items;
        doneToolbar.sizeToFit()
        
        txtSalaryPerHour.inputAccessoryView = doneToolbar;
        
    }
    
    func keyboardDoneButtonPressed(){
        txtSalaryPerHour.resignFirstResponder();
    }
    
    func textFieldShouldReturn(textField: UITextField) -> Bool {
        textField.resignFirstResponder();
        return true;
    }
    
    override func preferredStatusBarStyle() -> UIStatusBarStyle {
        return UIStatusBarStyle.LightContent;
    }
    
}
