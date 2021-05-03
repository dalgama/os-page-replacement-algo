#include <stdio.h>
#include <pthread.h>
#include <stdlib.h>
#include <unistd.h>
#include <time.h>
#include <semaphore.h>

// Parent process --> producer
// child  process --> consumer


int * arrPtr; //global variable pointer
sem_t mutex;


int fact(int n) {

   if ((n==0)||(n==1)){
        return 1;
   }else{
       return (n*fact(n-1));
   }

}

int * CatlanSeq(int n){

    arrPtr = (int*)malloc(n*sizeof(int));
    if(arrPtr == NULL){
        printf("Error! memory not allocated.");
        exit(0);
    }
    for(int i=1; i<=n; i++){
            arrPtr[i-1] = (fact(2*i)) / (fact(i+1)*fact(i));
    }

   return arrPtr;
}


int main (int ac, char **av){
    int input;
    arrPtr = NULL;

    if (ac == 2){
        if (sscanf (av [1], "%d", &input)== 1){

        }else fprintf(stderr, "Cannot translate argument\n");

    }else fprintf(stderr, "Invalid arguments\n");

    int fd[2], pid, ret;
    sem_init(&mutex, 0, 1); //Initialize mutex to size 1


    if( (ret = pipe(fd)) == -1){   //check if create pipe failed
            perror("Pipe failed");
            exit(1);
    }
    pid = fork();
    if (pid == -1){  //check if creating a child process failed
            perror("fork failed");
            exit(1);
    }

    if (pid>0){             // Parent (producer) process
            dup2(fd[0], 0); // Clone reading end of pipe to standard input of parent
            close(fd[1]);   // Close write discriptor to pipe
            close(fd[0]);   // Close read discriptor to pipe

            int n, result;
            read(0, &n, sizeof(n)) ; // read child's (consumer) requests

            sem_wait(&mutex);
            arrPtr = CatlanSeq(n);  // <--Critical section
            sem_post(&mutex);

            sleep(5);

            sem_wait(&mutex);
            printf("Catalan sequence: "); // <--Critical section
            for(int i=0; i<input; i++){   // <--Critical section
                printf("%d, ", arrPtr[i]);   // <--Critical section
            }
            sem_post(&mutex);

    }else if(pid==0){       // child (consumer) process
            dup2(fd[1],1);  // Clone writing end of the pipe to the standard output
            close(fd[1]);   // Close write discriptor to pipe
            close(fd[0]);   // Close read discriptor to pipe

            write(1, &input, sizeof(input)); // request amount of Catalan numbers

    }
    return (0);
}
