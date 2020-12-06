package practice2_2;
import  java.util.Scanner;
import  java.util.concurrent.*;
import java.util.concurrent.RecursiveTask;
public class p2_2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int x = scanner.nextInt();
        ForkJoinTask<Long> number=new practice2_2.SumTask(1,1000000000,x);
        Long result=ForkJoinPool.commonPool().invoke(number);
        System.out.println(result);
        /*for (long i = 1; i < 1000000000; i++) {
            if (contain(i, x)) ans += i;
        }
        System.out.println(ans);*/
    }
}
class SumTask extends RecursiveTask<Long>{
    static final int THRESHOLD=60;
    int start;
    int end;
    int x;
    public SumTask(int start, int end, int x) {
        this.start = start;
        this.end = end;
        this.x = x;
    }
    private static boolean contain(long num, int x) {
        return String.valueOf(num).contains(String.valueOf(x));
    }
    @Override
    protected Long compute(){
        if(end-start<=THRESHOLD){//任务足够小，直接计算
            long sum=0;
            for(int i=start;i<end;i++){
                if(contain(i,x)) sum+=i;
            }
            return sum;
        }
        //任务太大，一分为二
        int middle=(end+start)/2;
        practice2_2.SumTask subtask1=new practice2_2.SumTask(start,middle,x);
        practice2_2.SumTask subtask2=new practice2_2.SumTask(middle,end,x);
        invokeAll(subtask1,subtask2);
        Long subresult1=subtask1.join();
        Long subresult2=subtask2.join();
        Long result=subresult1+subresult2;
        return result;
    }
}