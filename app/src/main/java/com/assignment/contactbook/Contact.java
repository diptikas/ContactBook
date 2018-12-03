package com.assignment.contactbook;

import java.util.ArrayList;

public class Contact {
        public String id;
        public String name;
        public ArrayList<ContactPhone> numbers;

        public Contact(String id, String name) {
            this.id = id;
            this.name = name;
            this.numbers = new ArrayList<ContactPhone>();
        }

        @Override
        public String toString() {
            String result = name;
            if (numbers.size() > 0) {
                ContactPhone number = numbers.get(0);
                result += " (" + number.number + " - " + number.type + ")";
            }
            return result;
        }


        public void addNumber(String number, String type) {
            numbers.add(new ContactPhone(number, type));
        }
    }

