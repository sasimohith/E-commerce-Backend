#!/usr/bin/env python3
"""
Convert CSV file to Excel format
This script converts the generated CSV file to a proper Excel format.
"""

import csv
import json

def csv_to_excel_manual():
    """Convert CSV to a format that can be easily imported into Excel."""
    
    # Read the CSV file
    users = []
    with open('dummy_users_100.csv', 'r', encoding='utf-8') as csvfile:
        reader = csv.DictReader(csvfile)
        for row in reader:
            users.append(row)
    
    # Create a tab-delimited file that Excel can import easily
    excel_filename = 'dummy_users_100.txt'
    
    with open(excel_filename, 'w', encoding='utf-8') as txtfile:
        # Write header
        headers = ['Full Name', 'Email Address', 'Password', 'Confirm Password', 
                  'Phone Number', 'Country', 'Address', 'City', 'State', 'ZIP Code']
        txtfile.write('\t'.join(headers) + '\n')
        
        # Write data rows
        for user in users:
            row_data = [user[header] for header in headers]
            txtfile.write('\t'.join(row_data) + '\n')
    
    print(f"✅ Created tab-delimited file: {excel_filename}")
    print("💡 To import into Excel:")
    print("   1. Open Excel")
    print("   2. Go to Data > Get Data > From File > From Text/CSV")
    print(f"   3. Select the file: {excel_filename}")
    print("   4. Choose 'Tab' as the delimiter")
    print("   5. Click 'Load'")
    
    # Also create a formatted JSON for easy viewing
    json_filename = 'dummy_users_100.json'
    with open(json_filename, 'w', encoding='utf-8') as jsonfile:
        json.dump(users, jsonfile, indent=2, ensure_ascii=False)
    
    print(f"✅ Also created JSON file for reference: {json_filename}")
    
    return len(users)

def main():
    """Convert the CSV file to Excel-friendly format."""
    print("🔄 Converting CSV to Excel format...")
    
    try:
        user_count = csv_to_excel_manual()
        print(f"\n🎉 Successfully processed {user_count} user records!")
        print("\n📁 Files created:")
        print("   • dummy_users_100.csv  - Original CSV file")
        print("   • dummy_users_100.txt  - Tab-delimited for Excel import")
        print("   • dummy_users_100.json - JSON format for reference")
        
    except FileNotFoundError:
        print("❌ Error: dummy_users_100.csv file not found!")
        print("Please run 'python generate_dummy_users.py' first.")
    except Exception as e:
        print(f"❌ Error: {e}")

if __name__ == "__main__":
    main()
