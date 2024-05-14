/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javacodes;

/**
 *
 * @author malin
 */
public class Target2 {

    public static void targetAchieved(int[] array, int target) {
        int left = 0;
        int right = array.length - 1;
        while (left < right) {
            int sum = array[right] + array[left];
            if (sum == target) {
                System.out.println("A:" + array[left] + " B:" + array[right]);
                break;
            } else if (sum < target) {
                left++;
            } else {
                right--;
            }
        }
    }

    public static void main(String[] args) {
        int[] nums1 = {1, 2, 3, 4, 5, 6};
        int target1 = 10;
        int[] nums2 = {2, 7, 11, 15};
        int target2 = 9;
        targetAchieved(nums1, target1);
        targetAchieved(nums2, target2);
    }
}
