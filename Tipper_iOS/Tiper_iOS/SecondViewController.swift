//
//  SecondViewController.swift
//  Tiper_iOS
//
//  Created by גיל אושר on 22.12.2015.
//  Copyright © 2015 gil osher. All rights reserved.
//

import UIKit

class SecondViewController: UIViewController {
    
    var navBar: UINavigationBar!;
    
    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = UIColor(netHex: 0x2979FF);
        let statusBar = UIView(frame: CGRect(x: 0, y: 0, width: view.frame.width, height: UIApplication.sharedApplication().statusBarFrame.height));
        statusBar.backgroundColor = UIColor(netHex: 0x1F2839);
        view.addSubview(statusBar);
        
        view.backgroundColor = UIColor(netHex: 0x90CAF9);
        navBar = UINavigationBar(frame: CGRect(x: 0, y: UIApplication.sharedApplication().statusBarFrame.height, width: view.frame.width, height: 40))
        let navItem = UINavigationItem();
        navItem.title = "טיפר";
        navItem.leftBarButtonItem = UIBarButtonItem(title: "הגדרות", style: UIBarButtonItemStyle.Plain, target: self, action: "openSettings");
        navBar.items = [navItem];
        view.addSubview(navBar);
        
    }
    
    func openSettings(){
        presentViewController(SettingsViewController(), animated: true, completion: nil);
    }
    
    override func preferredStatusBarStyle() -> UIStatusBarStyle {
        return UIStatusBarStyle.LightContent;
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }


}

