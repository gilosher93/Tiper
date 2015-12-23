//
//  FirstViewController.swift
//  Tiper_iOS
//
//  Created by גיל אושר on 22.12.2015.
//  Copyright © 2015 gil osher. All rights reserved.
//

import UIKit

class FirstViewController: UIViewController {
    
    var statusBar: UIView!;
    var btnActive: UIButton!;
    var lblStartTime: UILabel!;
    var active: Bool!;
    var navBar: UINavigationBar!;
    
    //active view container
    var activeView: UIView!;
    //salary views
    var salaryView: UIView!;
    var lblSalaryValue: UILabel!;
    //tips views
    var tipsView: UIView!;
    var btnIncreaseTipsValue: UIButton!;
    var lblTipsValue: UILabel!;
    var btnDecreaseTipsValue: UIButton!;
    //summary views
    var summaryView: UIView!;
    var lblSummaryValue: UILabel!;
    
    //drag views
    var dragView: UIView!;
    var btn1Sheqel: UIButton!;
    var btn2Sheqel: UIButton!;
    var btn5Sheqel: UIButton!;
    var btn10Sheqel: UIButton!;
    var btn20Sheqel: UIButton!;
    var target: UIView!;
    var textInTarget: UILabel!;
    
    var lblNotActive: UILabel!;
    
    //main fields
    var dailyTips: Int = 0;
    var dailySalary: Float = 0;
    var dailySummary: Float = 0;
    var startTime: NSDate?;
    
    override func viewDidLoad() {
        super.viewDidLoad()
        active = false;
        
        statusBar = UIView(frame: CGRect(x: 0, y: 0, width: view.frame.width, height: UIApplication.sharedApplication().statusBarFrame.height));
        statusBar.backgroundColor = UIColor(netHex: 0x1F2839);
        view.addSubview(statusBar);
        
        view.backgroundColor = UIColor(netHex: 0x90CAF9);
        navBar = UINavigationBar(frame: CGRect(x: 0, y: UIApplication.sharedApplication().statusBarFrame.height, width: view.frame.width, height: 40))
        let navItem = UINavigationItem();
        navItem.title = "טיפר";
        navItem.leftBarButtonItem = UIBarButtonItem(title: "הגדרות", style: UIBarButtonItemStyle.Plain, target: self, action: "openSettings");
        navBar.items = [navItem];
        view.addSubview(navBar);
        
        btnActive = UIButton(type: UIButtonType.System);
        btnActive.frame = CGRect(x: view.frame.width - 80 - 5, y: navBar.frame.maxY + 10, width: 80, height: 35);
        btnActive.setTitle("התחל עבודה", forState: UIControlState.Normal);
        btnActive.addTarget(self, action: "checkIfActive", forControlEvents: UIControlEvents.TouchUpInside);
        btnActive.backgroundColor = UIColor(netHex: 0x1F2839);
        btnActive.tintColor = UIColor.whiteColor();
        btnActive.layer.cornerRadius = 15;
        view.addSubview(btnActive);
        lblStartTime = UILabel(frame: CGRect(x: 95, y: navBar.frame.maxY + 10, width: view.frame.width - 80*2, height: 35));
        createNotActiveLabel();
        createActiveView();
        if active!{
            connectUser();
        }else{
            view.addSubview(lblNotActive);
        }
    }
    
    override func preferredStatusBarStyle() -> UIStatusBarStyle {
        return UIStatusBarStyle.LightContent;
    }
    
    func createActiveView(){
        activeView = UIView(frame: CGRect(x: 15, y: btnActive.frame.maxY + 10, width: view.frame.width - 30, height: view.frame.height - btnActive.frame.height - 20 - statusBar.frame.height - navBar.frame.height - 60));
        
        salaryView = UIView(frame: CGRect(x: 5, y: 5, width: activeView.frame.width / 2 - 5, height: activeView.frame.height / 6));
        let lblSalaryTitle = UILabel(frame: CGRect(x: 0, y: 0, width: salaryView.frame.width, height: salaryView.frame.height / 2));
        lblSalaryTitle.text = "משכורת היום";
        lblSalaryTitle.textAlignment = .Center;
        lblSalaryTitle.font = UIFont.boldSystemFontOfSize(20);
        lblSalaryValue = UILabel(frame: CGRect(x: 0, y: lblSalaryTitle.frame.maxY, width: salaryView.frame.width, height: salaryView.frame.height / 2));
        lblSalaryValue.text = "0₪";
        lblSalaryValue.textAlignment = .Center;
        lblSalaryValue.font = UIFont.boldSystemFontOfSize(20);
        salaryView.addSubview(lblSalaryValue);
        salaryView.addSubview(lblSalaryTitle);
        
        tipsView = UIView(frame: CGRect(x: salaryView.frame.maxX, y: 5, width: activeView.frame.width / 2 - 5, height: activeView.frame.height / 6));
        let lblTipsTitle = UILabel(frame: CGRect(x: 0, y: 0, width: tipsView.frame.width, height: tipsView.frame.height / 2));
        lblTipsTitle.text = "טיפים היום";
        lblTipsTitle.textAlignment = .Center;
        lblTipsTitle.font = UIFont.boldSystemFontOfSize(20);
        btnDecreaseTipsValue = UIButton(type: UIButtonType.System);
        btnDecreaseTipsValue.frame = CGRect(x: 0, y: lblTipsTitle.frame.maxY, width: tipsView.frame.width / 3, height: tipsView.frame.height / 2)
        btnDecreaseTipsValue.tintColor = UIColor.blackColor();
        btnDecreaseTipsValue.setTitle("-", forState: UIControlState.Normal);
        btnDecreaseTipsValue.addTarget(self, action: "changeTipsValue:", forControlEvents: UIControlEvents.TouchUpInside);
        lblTipsValue = UILabel(frame: CGRect(x: btnDecreaseTipsValue.frame.maxX, y: lblTipsTitle.frame.maxY, width: tipsView.frame.width / 3, height: tipsView.frame.height / 2));
        lblTipsValue.text = "0₪";
        lblTipsValue.textAlignment = .Center;
        lblTipsValue.font = UIFont.boldSystemFontOfSize(20);
        btnIncreaseTipsValue = UIButton(type: UIButtonType.System);
        btnIncreaseTipsValue.frame = CGRect(x: lblTipsValue.frame.maxX, y: lblTipsTitle.frame.maxY, width: tipsView.frame.width / 3, height: tipsView.frame.height / 2)
        btnIncreaseTipsValue.tintColor = UIColor.blackColor();
        btnIncreaseTipsValue.setTitle("+", forState: UIControlState.Normal);
        btnIncreaseTipsValue.addTarget(self, action: "changeTipsValue:", forControlEvents: UIControlEvents.TouchUpInside);
        tipsView.addSubview(btnDecreaseTipsValue);
        tipsView.addSubview(lblTipsValue);
        tipsView.addSubview(btnIncreaseTipsValue);
        tipsView.addSubview(lblTipsTitle);
        
        summaryView = UIView(frame: CGRect(x: 5, y: tipsView.frame.maxY + 5, width: activeView.frame.width - 10, height: activeView.frame.height / 6));
        let lblSummaryTitle = UILabel(frame: CGRect(x: 0, y: 0, width: summaryView.frame.width, height: summaryView.frame.height / 2));
        lblSummaryTitle.text = "סה״כ היום";
        lblSummaryTitle.textAlignment = .Center;
        lblSummaryTitle.font = UIFont.boldSystemFontOfSize(20);
        lblSummaryValue = UILabel(frame: CGRect(x: 0, y: lblSummaryTitle.frame.maxY, width: summaryView.frame.width, height: summaryView.frame.height / 2));
        lblSummaryValue.text = "0₪";
        lblSummaryValue.textAlignment = .Center;
        lblSummaryValue.font = UIFont.boldSystemFontOfSize(20);
        summaryView.addSubview(lblSummaryTitle);
        summaryView.addSubview(lblSummaryValue);
        
        dragView = UIView(frame: CGRect(x: 0, y: summaryView.frame.maxY + 5, width: activeView.frame.width, height: activeView.frame.height / 6 * 4 - 15));
        target = UIView(frame: CGRect(x: (dragView.frame.width - 150) / 2, y: 25, width: 150, height: 150));
        target.backgroundColor = UIColor(netHex: 0xFFC107);
        target.layer.cornerRadius = 75;
        target.layer.borderColor = UIColor(netHex: 0x757575).CGColor;
        target.layer.borderWidth = 15;
        textInTarget = UILabel(frame: CGRect(x: (target.frame.width - 80 ) / 2, y: (target.frame.height - 50) / 2, width: 80, height: 50));
        textInTarget.text = dailyTips == 0 ? "אין טיפים בקופה" : "\(dailyTips)₪";
        textInTarget.textAlignment = .Center;
        textInTarget.font = UIFont.boldSystemFontOfSize(20);
        textInTarget.textColor = UIColor(netHex: 0x757575);
        textInTarget.numberOfLines = 2;
        target.addSubview(textInTarget);
        btn1Sheqel = UIButton(type: UIButtonType.Custom);
        btn1Sheqel.setBackgroundImage(UIImage(named: "sheqel1"), forState: UIControlState.Normal);
        btn1Sheqel.frame = CGRect(x: 2, y: dragView.frame.height / 8, width: 50, height: 50);
        btn5Sheqel = UIButton(type: UIButtonType.Custom);
        btn5Sheqel.setBackgroundImage(UIImage(named: "sheqel5"), forState: UIControlState.Normal);
        btn5Sheqel.frame = CGRect(x: 2, y: dragView.frame.height / 2, width: 50, height: 50);
        btn2Sheqel = UIButton(type: UIButtonType.Custom);
        btn2Sheqel.setBackgroundImage(UIImage(named: "sheqel2"), forState: UIControlState.Normal);
        btn2Sheqel.frame = CGRect(x: dragView.frame.width - 52, y: dragView.frame.height / 8, width: 50, height: 50);
        btn10Sheqel = UIButton(type: UIButtonType.Custom);
        btn10Sheqel.setBackgroundImage(UIImage(named: "sheqel10"), forState: UIControlState.Normal);
        btn10Sheqel.frame = CGRect(x: dragView.frame.width - 52, y: dragView.frame.height / 2, width: 50, height: 50);
        btn20Sheqel = UIButton(type: UIButtonType.Custom);
        btn20Sheqel.setBackgroundImage(UIImage(named: "sheqel20"), forState: UIControlState.Normal);
        btn20Sheqel.frame = CGRect(x: (dragView.frame.width - 114 ) / 2, y: dragView.frame.height - 60, width: 114, height: 60);
        btn1Sheqel.addTarget(self, action: "handleButtons:", forControlEvents: UIControlEvents.TouchUpInside);
        btn2Sheqel.addTarget(self, action: "handleButtons:", forControlEvents: UIControlEvents.TouchUpInside);
        btn5Sheqel.addTarget(self, action: "handleButtons:", forControlEvents: UIControlEvents.TouchUpInside);
        btn10Sheqel.addTarget(self, action: "handleButtons:", forControlEvents: UIControlEvents.TouchUpInside);
        btn20Sheqel.addTarget(self, action: "handleButtons:", forControlEvents: UIControlEvents.TouchUpInside);
        dragView.addSubview(target);
        dragView.addSubview(btn20Sheqel);
        dragView.addSubview(btn10Sheqel);
        dragView.addSubview(btn2Sheqel);
        dragView.addSubview(btn5Sheqel);
        dragView.addSubview(btn1Sheqel);
        activeView.addSubview(dragView);
        activeView.addSubview(summaryView);
        activeView.addSubview(salaryView);
        activeView.addSubview(tipsView);
    }
    
    func createNotActiveLabel(){
        lblNotActive = UILabel(frame: CGRect(x: 15, y: btnActive.frame.maxY + 10, width: view.frame.width - 30, height: 70));
        lblNotActive.contentMode = .Redraw
        lblNotActive.numberOfLines = 2;
        lblNotActive.textAlignment = .Right;
        lblNotActive.text = "לחץ על ״התחל עבודה״ בכדי להתחיל את המשמרת";
        lblNotActive.textColor = UIColor.blueColor();
        lblNotActive.font = UIFont.boldSystemFontOfSize(20);
    }
    
    func changeTipsValue(sender: UIButton){
        if(sender == btnDecreaseTipsValue){
            if(dailyTips > 0){
                dailyTips--;
                lblTipsValue.text = "\(dailyTips)₪";
            }
        }else{
            dailyTips++;
            lblTipsValue.text = "\(dailyTips)₪";
        }
    }
    
    func openSettings(){
        presentViewController(SettingsViewController(), animated: true, completion: nil);
    }
    
    func connectUser(){
        UIView.animateWithDuration(0.5, animations: { [weak self]() -> Void in
            self!.btnActive.frame = CGRect(x: 5, y: self!.navBar.frame.maxY + 10, width: 80, height: 35)
            }, completion: { [weak self](finish: Bool) -> Void in
                self!.lblNotActive.removeFromSuperview();
                self!.view.addSubview(self!.activeView);
                let components = NSCalendar.currentCalendar().componentsInTimeZone(NSTimeZone(), fromDate: self!.startTime!);
                let hourString = FirstViewController.getHourString(components.hour, minutes: components.minute);
                self!.lblStartTime.text = "שעת התחלה \(hourString)";
                self!.view.addSubview(self!.lblStartTime);
            })
    }
    
    func disconnectUser(){
        UIView.animateWithDuration(0.5, animations: { [weak self]() -> Void in
            self!.btnActive.frame = CGRect(x: self!.view.frame.width - 80 - 5, y: self!.navBar.frame.maxY + 10, width: 80, height: 35)
            }, completion: { [weak self](finish: Bool) -> Void in
                self!.activeView.removeFromSuperview();
                self!.view.addSubview(self!.lblNotActive);
                self!.lblStartTime.removeFromSuperview();
            })
    }

    func checkIfActive(){
        if !active{
            active = true;
            startTime = NSDate();
            btnActive.setTitle("סיים עבודה", forState: UIControlState.Normal);
            connectUser();
        }else{
            active = false;
            btnActive.setTitle("התחל עבודה", forState: UIControlState.Normal);
            disconnectUser();
        }
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    static func getHourString(hour: Int, minutes: Int)-> String{
        var strHour = "\(hour)";
        if hour < 10{
            strHour = "0\(strHour)";
        }
        var strMinutes = "\(minutes)";
        if minutes < 10{
            strMinutes = "0\(strMinutes)";
        }
        return "\(strHour):\(strMinutes)";
    }
    
    func handleButtons(sender: UIButton){
        let senderCenter = sender.center;
        UIView.animateWithDuration(0.5, animations: { [weak self]() -> Void in
            sender.center = self!.target.center;
            sender.alpha = 0;
            }) { [weak self](finish: Bool) -> Void in
                sender.center = senderCenter;
                sender.alpha = 1;
                switch sender{
                case self!.btn1Sheqel:
                    self!.dailyTips+=1;
                case self!.btn2Sheqel:
                    self!.dailyTips+=2;
                case self!.btn5Sheqel:
                    self!.dailyTips+=5;
                case self!.btn10Sheqel:
                    self!.dailyTips+=10;
                case self!.btn20Sheqel:
                    self!.dailyTips+=20;
                default:
                    break;
                }
                self!.lblTipsValue.text = "\(self!.dailyTips)₪";
                self!.textInTarget.text = "\(self!.dailyTips)₪";
        }
        
    }
}

extension UIColor {
    convenience init(red: Int, green: Int, blue: Int) {
        assert(red >= 0 && red <= 255, "Invalid red component")
        assert(green >= 0 && green <= 255, "Invalid green component")
        assert(blue >= 0 && blue <= 255, "Invalid blue component")
        
        self.init(red: CGFloat(red) / 255.0, green: CGFloat(green) / 255.0, blue: CGFloat(blue) / 255.0, alpha: 1.0)
    }
    
    convenience init(netHex:Int) {
        self.init(red:(netHex >> 16) & 0xff, green:(netHex >> 8) & 0xff, blue:netHex & 0xff)
    }
}
