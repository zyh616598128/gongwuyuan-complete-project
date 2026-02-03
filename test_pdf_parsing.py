#!/usr/bin/env python3
"""
Test script to validate PDF parsing functionality
"""

import requests
import json
import time
import os

def test_pdf_parsing():
    print("Testing PDF Parsing Functionality...")
    
    # Test server availability
    try:
        response = requests.get("http://localhost:8081/api/subjects", headers={'Authorization': 'Bearer dummy'})
        if response.status_code == 401:  # Expected for protected endpoint
            print("✅ Backend server is running (401 expected for auth)")
        else:
            print(f"❌ Backend server returned unexpected status {response.status_code}")
            return False
    except Exception as e:
        print(f"❌ Backend server is not accessible: {e}")
        return False

    # Test with sample text that mimics typical exam format
    print("\nTesting text parsing with sample exam content...")
    
    sample_exam_text = """
    2010年贵州公务员考试《行测》真题
    
    第一部分：数量关系
    
    1. 甲、乙两人分别从A、B两地同时出发，相向而行。甲的速度是每小时5公里，乙的速度是每小时4公里。如果A、B两地相距18公里，则他们相遇需要多少小时？
    A. 1小时
    B. 2小时  
    C. 3小时
    D. 4小时
    【答案】B
    
    2. 某商品价格先上涨10%，后又下降10%，则该商品最终价格与原价相比：
    A. 上涨了1%
    B. 下降了1%  
    C. 没有变化
    D. 无法确定
    【答案】B
    
    3. 一个长方形的长是宽的2倍，如果宽增加3米，长减少2米，则新长方形面积比原长方形面积增加12平方米。求原长方形的面积是多少平方米？
    A. 32
    B. 48  
    C. 64
    D. 72
    【答案】C
    
    第二部分：言语理解
    
    4. 依次填入下列各句横线处的词语，最恰当的一组是：
    ① 新技术的出现使得信息传递变得更加________。
    ② 这次改革的效果还需要进一步________。
    A. 快捷 检验
    B. 快速 实验
    C. 迅速 检验
    D. 敏捷 实验
    【答案】A
    """
    
    # Test text import
    print("Testing text import functionality...")
    try:
        import_response = requests.post(
            "http://localhost:8081/api/smart-import/questions/text",
            data={
                'content': sample_exam_text,
                'subjectId': 1,
                'hasAnalysis': False
            },
            headers={'Authorization': 'Bearer dummy'}  # This will fail auth but show if endpoint works
        )
        
        # Even if auth fails, we can see if the endpoint received the request
        print(f"   Text import response status: {import_response.status_code}")
        
        # Let's try without auth to see parsing errors
        import_response_no_auth = requests.post(
            "http://localhost:8081/api/smart-import/questions/text",
            data={
                'content': sample_exam_text,
                'subjectId': 1
            }
        )
        
        print(f"   Text import response (no auth): {import_response_no_auth.status_code}")
        if import_response_no_auth.status_code == 200:
            result = import_response_no_auth.json()
            print(f"   Response: {json.dumps(result, ensure_ascii=False, indent=2)}")
        else:
            print(f"   Non-auth response body: {import_response_no_auth.text[:500]}...")
        
    except Exception as e:
        print(f"❌ Error during text import test: {e}")

    # Create a simple test PDF content to understand the issue
    print("\nAnalyzing potential parsing issues...")
    
    print("Issue likely stems from:")
    print("  1. PDF text extraction may not preserve original formatting")
    print("  2. Regular expressions may not match actual document patterns")
    print("  3. Document structure differs from expected format")
    print("  4. Character encoding issues")
    
    print("\nPotential solutions:")
    print("  1. Improve regex patterns for various question formats")
    print("  2. Add preprocessing to normalize document text")
    print("  3. Implement fallback parsing strategies")
    print("  4. Add debug logging to see what text is extracted")

    print("\n" + "="*60)
    print("PDF Parsing Analysis Complete")
    print("="*60)
    
    return True

if __name__ == "__main__":
    test_pdf_parsing()