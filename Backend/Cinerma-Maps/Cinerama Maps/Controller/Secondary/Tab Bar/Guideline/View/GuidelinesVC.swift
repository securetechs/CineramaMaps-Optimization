//
//  GuidelinesVC.swift
//  Cinerama Maps
//
//  Created by Techimmense Software Solutions on 28/08/24.
//

import UIKit
import WebKit
import SDWebImage

class GuidelinesVC: UIViewController, WKNavigationDelegate, UIGestureRecognizerDelegate {
    
    // MARK: - OUTLETS
    @IBOutlet weak var lbl_Title: UILabel!
    @IBOutlet weak var lbl_Date: UILabel!
    @IBOutlet weak var wv_Container: UIView!
    @IBOutlet weak var img: UIImageView!
    @IBOutlet weak var lbl_Headline: UILabel!
    @IBOutlet weak var discountButtonVw: UIView!
    @IBOutlet weak var btn_DiscountOt: UIButton!
    @IBOutlet weak var wv_ContainerHeightConstraint: NSLayoutConstraint!
    
    // MARK: - VARIABLES
    var titleVal: String = ""
    var dateTime: String = ""
    var descriptionVal: String = ""
    var placeImg: String = ""
    var offerCode: String = ""
    var isFrom: String = ""
    
    private var hasLoadedOnce = false
    private var webView: WKWebView!
    
    // ✅ Cache key = unique identifier per content
    private var cacheKey: String {
        return "\(isFrom)_\(titleVal)".trimmingCharacters(in: .whitespacesAndNewlines)
    }
    
    // ✅ Shared warm process pool + persistent data store
    private static let sharedProcessPool = WKProcessPool()
    private static let sharedDataStore = WKWebsiteDataStore.default()
    
    // MARK: - LIFE CYCLE
    override func viewDidLoad() {
        super.viewDidLoad()
        setupBackGesture()
        
        guard !hasLoadedOnce else { return }
        hasLoadedOnce = true
        
        setupWebView()
        setGuideTips()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        self.tabBarController?.tabBar.isHidden = true
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        webView?.stopLoading()
    }
    
    deinit {
        webView?.navigationDelegate = nil
        webView?.uiDelegate = nil
        webView?.stopLoading()
        webView?.removeFromSuperview()
        webView = nil
        print("WKWebView Deallocated ✅")
    }
    
    // MARK: - ACTIONS
    @IBAction func btn_Back(_ sender: UIButton) {
        if webView.canGoBack {
            webView.goBack()
        } else {
            navigationController?.popViewController(animated: true)
        }
    }
}

// MARK: - Setup UI
extension GuidelinesVC {
    private func setGuideTips() {
        wv_ContainerHeightConstraint.constant = 0
        wv_Container.isHidden = true
        wv_Container.semanticContentAttribute = .forceLeftToRight
        view.layoutIfNeeded()
        
        if isFrom == "Advertisement" {
            lbl_Date.isHidden = true
            lbl_Headline.text = R.string.localizable.advertisementDetails()
            discountButtonVw.isHidden = true
            
        } else if isFrom == "Guideline" {
            lbl_Date.isHidden = false
            lbl_Date.text = "\(self.dateTime)"
            lbl_Headline.text = R.string.localizable.guidelinesAndTips()
            lbl_Title.textColor = R.color.main()
            discountButtonVw.isHidden = true
            
        } else {
            lbl_Headline.text = R.string.localizable.offerDetails()
            lbl_Date.isHidden = true
            btn_DiscountOt.setTitle(offerCode, for: .normal)
            discountButtonVw.isHidden = false
        }
        
        lbl_Title.text = titleVal
        loadHTML(descriptionVal)
        
        if Router.BASE_IMAGE_URL != placeImg {
            img.sd_setImage(with: URL(string: placeImg), placeholderImage: nil)
        } else {
            img.image = R.image.blank()
        }
    }
}

// MARK: - WebView Setup
extension GuidelinesVC {
    private func setupWebView() {
        let config = WKWebViewConfiguration()
        config.processPool = GuidelinesVC.sharedProcessPool
        config.websiteDataStore = GuidelinesVC.sharedDataStore  // ✅ persistent cache
        
        config.allowsInlineMediaPlayback = true
        config.mediaTypesRequiringUserActionForPlayback = []
        config.allowsPictureInPictureMediaPlayback = true
        config.allowsAirPlayForMediaPlayback = true
        
        let prefs = WKWebpagePreferences()
        prefs.allowsContentJavaScript = true
        config.defaultWebpagePreferences = prefs
        
        webView = WKWebView(frame: .zero, configuration: config)
        
        // ✅ CRITICAL: Set UserAgent to mimic Safari for YouTube playback
        webView.customUserAgent = "Mozilla/5.0 (iPhone; CPU iPhone OS 17_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.0 Mobile/15E148 Safari/604.1"
        
        webView.semanticContentAttribute = .forceLeftToRight
        webView.navigationDelegate = self
        webView.allowsBackForwardNavigationGestures = false
        webView.scrollView.contentInsetAdjustmentBehavior = .never
        webView.backgroundColor = .clear
        webView.isOpaque = false
        webView.scrollView.isScrollEnabled = false
        webView.scrollView.bounces = false
        
        wv_Container.addSubview(webView)
        webView.translatesAutoresizingMaskIntoConstraints = false
        NSLayoutConstraint.activate([
            webView.topAnchor.constraint(equalTo: wv_Container.topAnchor),
            webView.bottomAnchor.constraint(equalTo: wv_Container.bottomAnchor),
            webView.leadingAnchor.constraint(equalTo: wv_Container.leadingAnchor),
            webView.trailingAnchor.constraint(equalTo: wv_Container.trailingAnchor)
        ])
        
        webView.semanticContentAttribute = .forceLeftToRight
        webView.scrollView.semanticContentAttribute = .forceLeftToRight
    }
}

// MARK: - WKNavigationDelegate
extension GuidelinesVC {
    
    func webView(_ webView: WKWebView, didFinish navigation: WKNavigation!) {
        
        webView.evaluateJavaScript (
            "Math.max(document.body.scrollHeight, document.documentElement.scrollHeight)"
        ) { [weak self] result, _ in
            
            guard let self else { return }
            guard let height = result as? CGFloat, height > 10 else { return }
            
            // Update UI smoothly
            self.wv_Container.isHidden = false
            self.wv_ContainerHeightConstraint.constant = height
            UIView.animate(withDuration: 0.2) {
                self.view.layoutIfNeeded()
            }
            
            // ⭐ SAVE HTML + HEIGHT TO CACHE
            webView.evaluateJavaScript("document.documentElement.outerHTML.toString()") { htmlResult, _ in
                if let finalHTML = htmlResult as? String {
                    WebViewCacheManager.shared.save (
                        html: finalHTML,
                        height: height,
                        forKey: self.cacheKey
                    )
                }
            }
        }
    }
    
    func webView(_ webView: WKWebView, didFail navigation: WKNavigation!, withError error: Error) {
        print("WebView navigation failed: \(error.localizedDescription)")
    }
}

// MARK: - Load HTML
extension GuidelinesVC {
    
    private func extractYouTubeID(from urlString: String) -> String? {
        let pattern = #"(?<=v=)[^&#]+|(?<=be/)[^&#]+|(?<=embed/)[^&#]+|(?<=shorts/)[^&#]+"#
        let regex = try? NSRegularExpression(pattern: pattern, options: .caseInsensitive)
        let range = NSRange(location: 0, length: urlString.utf16.count)
        if let match = regex?.firstMatch(in: urlString, options: [], range: range) {
            return (urlString as NSString).substring(with: match.range)
        }
        return nil
    }
    
    private func processYouTubeContent(_ html: String) -> String {
        // 1. If it's a raw YouTube link (common case), convert it to an embed iframe
        let trimmed = html.trimmingCharacters(in: .whitespacesAndNewlines)
        if (trimmed.lowercased().hasPrefix("http") && (trimmed.contains("youtube.com") || trimmed.contains("youtu.be"))),
           let videoID = extractYouTubeID(from: trimmed),
           !trimmed.contains("<iframe") {
            
            return """
            <div class="video-container">
                <iframe
                    src="https://www.youtube-nocookie.com/embed/\(videoID)?playsinline=1&rel=0&modestbranding=1"
                    allow="autoplay; fullscreen; encrypted-media; picture-in-picture"
                    allowfullscreen
                    frameborder="0">
                </iframe>
            </div>
            """
        }
        
        // 2. If it's HTML with multiple YouTube links, we could use regex to replace them
        let pattern = #"(https?://(?:www\.)?(?:youtube\.com/(?:watch\?v=|shorts/|embed/)|youtu\.be/)([a-zA-Z0-9_-]{11})[^\s<]*)"#
        guard let regex = try? NSRegularExpression(pattern: pattern, options: .caseInsensitive) else {
            return html
        }
        
        let nsHtml = html as NSString
        let matches = regex.matches(in: html, options: [], range: NSRange(location: 0, length: html.utf16.count))
        
        var result = html
        var offset = 0
        
        for match in matches {
            let matchRange = match.range(at: 1)
            let videoIDRange = match.range(at: 2)
            
            let videoID = nsHtml.substring(with: videoIDRange)
            let fullURL = nsHtml.substring(with: matchRange)
            
            // Skip if already in an iframe or if it's already an embed link
            if html.contains("src=\"\(fullURL)\"") || html.contains("src='\(fullURL)'") || html.contains("embed/\(videoID)") {
                continue
            }
            
            let iframe = """
            <div class="video-container">
                <iframe
                    src="https://www.youtube-nocookie.com/embed/\(videoID)?playsinline=1&rel=0&modestbranding=1"
                    allow="autoplay; fullscreen; encrypted-media; picture-in-picture"
                    allowfullscreen
                    frameborder="0">
                </iframe>
            </div>
            """
            
            let adjustedRange = NSRange(location: matchRange.location + offset, length: matchRange.length)
            result = (result as NSString).replacingCharacters(in: adjustedRange, with: iframe)
            offset += iframe.count - matchRange.length
        }
        
        return result
    }
    
    private func loadHTML(_ html: String) {
        
        let cleanHTML = html.trimmingCharacters(in: .whitespacesAndNewlines)
        
        guard !cleanHTML.isEmpty, cleanHTML.uppercased() != "NA" else {
            wv_ContainerHeightConstraint.constant = 0
            wv_Container.isHidden = true
            return
        }
        
        let processedYouTube = processYouTubeContent(cleanHTML)
        
        let normalizedHTML = processedYouTube
            .replacingOccurrences(of: "dir=\"rtl\"", with: "")
            .replacingOccurrences(of: "dir='rtl'", with: "")
            .replacingOccurrences(of: "dir=\"ltr\"", with: "")
            .replacingOccurrences(of: "dir='ltr'", with: "")
            .replacingOccurrences(of: "dir=\"auto\"", with: "")
            .replacingOccurrences(of: "dir='auto'", with: "")
        
        let styledHTML = buildStyledHTML(normalizedHTML)
        let baseURL = URL(string: "https://www.youtube-nocookie.com")
        
        // 🔥 STEP 1 — SHOW CACHE INSTANTLY
        if let cache = WebViewCacheManager.shared.load(forKey: cacheKey) {
            wv_Container.isHidden = false
            wv_ContainerHeightConstraint.constant = cache.height   // ⭐ INSTANT HEIGHT
            webView.loadHTMLString(cache.html, baseURL: baseURL)
            view.layoutIfNeeded()
        }
        
        // 🔥 STEP 2 — LOAD FRESH IN BACKGROUND
        webView.loadHTMLString(styledHTML, baseURL: baseURL)
    }
    
    private func buildStyledHTML(_ content: String) -> String {
        return """
        <html>
        <head>
        <meta name="viewport"
              content="width=device-width,
                       initial-scale=1.0,
                       maximum-scale=1.0,
                       minimum-scale=1.0,
                       user-scalable=no">
        
        <style>
        
            * {
                box-sizing: border-box;
                margin: 0;
                padding: 0;
            }
        
            html, body {
                width: 100%;
                height: auto;
                overflow: hidden;
        
                direction: ltr !important;
                text-align: left !important;
                unicode-bidi: embed !important;
            }
        
            body {
                font-family: -apple-system,
                             "Helvetica Neue",
                             Arial,
                             sans-serif;
        
                font-size: 16px;
                line-height: 1.6;
                color: #1A1A1A;
                background-color: transparent;
            }
        
            h1 {
                font-size: 20px;
                font-weight: 700;
                color: #E8510A;
                margin: 0 0 8px 0;
                line-height: 1.3;
            }
        
            h2 {
                font-size: 17px;
                font-weight: 700;
                color: #E8510A;
                margin: 16px 0 6px 0;
                line-height: 1.35;
            }
        
            h3 {
                font-size: 15px;
                font-weight: 600;
                color: #E8510A;
                margin: 12px 0 4px 0;
                line-height: 1.35;
            }
        
            p {
                font-size: 15px;
                color: #1A1A1A;
                margin: 0 0 10px 0;
            }
        
            ul, ol {
                font-size: 15px;
                color: #1A1A1A;
                margin: 4px 0 10px 0;
                padding-left: 20px !important;
            }
        
            li {
                margin-bottom: 4px;
            }
        
            strong, b {
                color: #1A1A1A;
                font-weight: 700;
            }
        
            hr {
                border: none;
                border-top: 1px solid #E0E0E0;
                margin: 12px 0;
            }
        
            img {
                max-width: 100%;
                height: auto;
                border-radius: 6px;
                display: block;
                margin-left: auto;
                margin-right: auto;
            }
        
            .video-container {
                position: relative;
                padding-bottom: 56.25%; /* 16:9 Aspect Ratio */
                height: 0;
                overflow: hidden;
                max-width: 100%;
                background: #000;
                border-radius: 8px;
                margin-bottom: 12px;
            }
        
            .video-container iframe {
                position: absolute;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                border: 0;
            }
        
            iframe {
                max-width: 100%;
                display: block;
                margin-left: auto;
                margin-right: auto;
            }
        
            a {
                color: #E8510A;
                text-decoration: none;
            }
        
            a:active {
                opacity: 0.7;
            }
        
        </style>
        </head>
        
        <body>
            \(content)
        </body>
        
        </html>
        """
    }
}

// MARK: - Back Gesture
extension GuidelinesVC {
    
    private func setupBackGesture() {
        navigationController?.interactivePopGestureRecognizer?.delegate = self
    }
    
    func gestureRecognizerShouldBegin(_ gestureRecognizer: UIGestureRecognizer) -> Bool {
        if webView.canGoBack {
            webView.goBack()
            return false
        }
        return true
    }
}

// MARK: - Pre-warm
extension GuidelinesVC {
    
    /// Call once from AppDelegate or root TabBarVC
    static func preWarm() {
        let config = WKWebViewConfiguration()
        config.processPool = GuidelinesVC.sharedProcessPool
        config.websiteDataStore = GuidelinesVC.sharedDataStore
        
        config.allowsInlineMediaPlayback = true
        config.mediaTypesRequiringUserActionForPlayback = []
        config.allowsPictureInPictureMediaPlayback = true
        config.allowsAirPlayForMediaPlayback = true
        
        let warmUp = WKWebView(frame: .zero, configuration: config)
        warmUp.customUserAgent = "Mozilla/5.0 (iPhone; CPU iPhone OS 17_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.0 Mobile/15E148 Safari/604.1"
        warmUp.loadHTMLString("<html><body></body></html>", baseURL: nil)
    }
}
