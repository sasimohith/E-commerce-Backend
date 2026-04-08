#!/usr/bin/env python3
"""
Script to generate dummy user data for e-commerce platform testing.
Creates 100 fake users with realistic data matching the registration form fields.
Uses only built-in Python libraries - no external dependencies required.
"""

import csv
import random
import string

# Sample data pools for generating realistic dummy data
FIRST_NAMES = [
    'James', 'Mary', 'John', 'Patricia', 'Robert', 'Jennifer', 'Michael', 'Linda', 'David', 'Elizabeth',
    'William', 'Barbara', 'Richard', 'Susan', 'Joseph', 'Jessica', 'Thomas', 'Sarah', 'Christopher', 'Karen',
    'Charles', 'Nancy', 'Daniel', 'Lisa', 'Matthew', 'Betty', 'Anthony', 'Helen', 'Mark', 'Sandra',
    'Donald', 'Donna', 'Steven', 'Carol', 'Paul', 'Ruth', 'Andrew', 'Sharon', 'Joshua', 'Michelle',
    'Kenneth', 'Laura', 'Kevin', 'Emily', 'Brian', 'Kimberly', 'George', 'Deborah', 'Timothy', 'Dorothy'
]

LAST_NAMES = [
    'Smith', 'Johnson', 'Williams', 'Brown', 'Jones', 'Garcia', 'Miller', 'Davis', 'Rodriguez', 'Martinez',
    'Hernandez', 'Lopez', 'Gonzalez', 'Wilson', 'Anderson', 'Thomas', 'Taylor', 'Moore', 'Jackson', 'Martin',
    'Lee', 'Perez', 'Thompson', 'White', 'Harris', 'Sanchez', 'Clark', 'Ramirez', 'Lewis', 'Robinson',
    'Walker', 'Young', 'Allen', 'King', 'Wright', 'Scott', 'Torres', 'Nguyen', 'Hill', 'Flores',
    'Green', 'Adams', 'Nelson', 'Baker', 'Hall', 'Rivera', 'Campbell', 'Mitchell', 'Carter', 'Roberts'
]

CITIES = [
    'New York', 'Los Angeles', 'Chicago', 'Houston', 'Phoenix', 'Philadelphia', 'San Antonio', 'San Diego',
    'Dallas', 'San Jose', 'Austin', 'Jacksonville', 'Fort Worth', 'Columbus', 'Charlotte', 'Seattle',
    'Denver', 'Boston', 'Nashville', 'Baltimore', 'Portland', 'Oklahoma City', 'Las Vegas', 'Detroit',
    'Memphis', 'Louisville', 'Milwaukee', 'Albuquerque', 'Tucson', 'Fresno', 'Sacramento', 'Mesa',
    'Kansas City', 'Atlanta', 'Long Beach', 'Colorado Springs', 'Raleigh', 'Miami', 'Virginia Beach', 'Omaha'
]

STATES = [
    'Alabama', 'Alaska', 'Arizona', 'Arkansas', 'California', 'Colorado', 'Connecticut', 'Delaware',
    'Florida', 'Georgia', 'Hawaii', 'Idaho', 'Illinois', 'Indiana', 'Iowa', 'Kansas', 'Kentucky',
    'Louisiana', 'Maine', 'Maryland', 'Massachusetts', 'Michigan', 'Minnesota', 'Mississippi',
    'Missouri', 'Montana', 'Nebraska', 'Nevada', 'New Hampshire', 'New Jersey', 'New Mexico',
    'New York', 'North Carolina', 'North Dakota', 'Ohio', 'Oklahoma', 'Oregon', 'Pennsylvania',
    'Rhode Island', 'South Carolina', 'South Dakota', 'Tennessee', 'Texas', 'Utah', 'Vermont',
    'Virginia', 'Washington', 'West Virginia', 'Wisconsin', 'Wyoming'
]

STREET_NAMES = [
    'Main St', 'Oak Ave', 'Pine St', 'Maple Ave', 'Cedar St', 'Elm St', 'Washington Ave', 'Park Ave',
    'Lincoln St', 'Jefferson St', 'Adams St', 'Jackson Ave', 'Madison St', 'Monroe St', 'Franklin Ave',
    'Roosevelt St', 'Church St', 'Spring St', 'High St', 'School St', 'State St', 'Broad St', 'Union St'
]

EMAIL_DOMAINS = ['gmail.com', 'yahoo.com', 'hotmail.com', 'outlook.com', 'aol.com', 'icloud.com']

def generate_password(length=10):
    """Generate a random password with letters, numbers, and special characters."""
    characters = string.ascii_letters + string.digits + "!@#$%&*"
    return ''.join(random.choice(characters) for _ in range(length))

def generate_phone():
    """Generate a US phone number in format (XXX) XXX-XXXX"""
    area_code = random.randint(200, 999)
    exchange = random.randint(200, 999)
    number = random.randint(1000, 9999)
    return f"({area_code}) {exchange}-{number}"

def generate_email(first_name, last_name):
    """Generate a realistic email address"""
    username_formats = [
        f"{first_name.lower()}.{last_name.lower()}",
        f"{first_name.lower()}{last_name.lower()}",
        f"{first_name.lower()}{random.randint(1, 999)}",
        f"{first_name.lower()}.{last_name.lower()}{random.randint(1, 99)}"
    ]
    username = random.choice(username_formats)
    domain = random.choice(EMAIL_DOMAINS)
    return f"{username}@{domain}"

def generate_address():
    """Generate a realistic street address"""
    street_number = random.randint(1, 9999)
    street_name = random.choice(STREET_NAMES)
    return f"{street_number} {street_name}"

def generate_zipcode():
    """Generate a 5-digit ZIP code"""
    return f"{random.randint(10000, 99999)}"

def generate_dummy_users(count=100):
    """Generate specified number of dummy users with realistic data."""
    users = []
    
    for i in range(count):
        first_name = random.choice(FIRST_NAMES)
        last_name = random.choice(LAST_NAMES)
        full_name = f"{first_name} {last_name}"
        password = generate_password()
        
        user = {
            'Full Name': full_name,
            'Email Address': generate_email(first_name, last_name),
            'Password': password,
            'Confirm Password': password,  # Same as password
            'Phone Number': generate_phone(),
            'Country': 'USA',  # Default as shown in the form
            'Address': generate_address(),
            'City': random.choice(CITIES),
            'State': random.choice(STATES),
            'ZIP Code': generate_zipcode()
        }
        users.append(user)
    
    return users

def save_to_csv(users_data, filename='dummy_users_100.csv'):
    """Save users data to CSV file that can be opened in Excel."""
    fieldnames = [
        'Full Name', 'Email Address', 'Password', 'Confirm Password', 
        'Phone Number', 'Country', 'Address', 'City', 'State', 'ZIP Code'
    ]
    
    with open(filename, 'w', newline='', encoding='utf-8') as csvfile:
        writer = csv.DictWriter(csvfile, fieldnames=fieldnames)
        writer.writeheader()
        writer.writerows(users_data)
    
    return filename

def main():
    """Generate dummy users and save to CSV file."""
    print("🚀 Generating 100 dummy users...")
    
    # Generate the dummy data
    users_data = generate_dummy_users(100)
    
    # Save to CSV file (can be opened in Excel)
    csv_filename = save_to_csv(users_data)
    
    print(f"✅ Successfully generated {len(users_data)} dummy users!")
    print(f"📊 Data saved to: {csv_filename}")
    print(f"💡 You can open this CSV file in Excel or any spreadsheet application")
    print(f"\n📋 Generated fields:")
    print("   • Full Name")
    print("   • Email Address") 
    print("   • Password")
    print("   • Confirm Password")
    print("   • Phone Number")
    print("   • Country (USA)")
    print("   • Address")
    print("   • City")
    print("   • State")
    print("   • ZIP Code")
    
    # Display sample of first 3 records
    print(f"\n🔍 Sample data (first 3 records):")
    for i, user in enumerate(users_data[:3]):
        print(f"\n  User {i+1}:")
        for field, value in user.items():
            print(f"    {field}: {value}")

if __name__ == "__main__":
    main()
