#include <string.h>
#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include <string.h>
#include <sys/utsname.h>
#include <time.h>
#include <dirent.h>
#include <limits.h>
#include <sys/types.h>
#include <dirent.h>
#include <errno.h>
#include </Users/keithmartin/Desktop/tinydir.h>


struct node {
    
    char *key;
    
    char *contents;
    
    struct node *next;
    
};




struct utsname unameData;


void insert(struct node **head, char *key, char *contents){
    
    struct node *new;
    struct node *prev;
    struct node *next;
    
    if (*head == NULL) {
        new = malloc(sizeof(struct node));
        new->key = malloc(sizeof(char) * strlen(key));
        new->contents = malloc(sizeof(char) * strlen(contents));
        strcpy(new->key, key);
        strcpy(new->contents, contents);
        *head = new;
     
    } else if (strcmp((*head)->key, key) > 0){
        new = malloc(sizeof *new );
        new->key = malloc(sizeof(char) * strlen(key));
        new->contents = malloc(sizeof(char) * strlen(contents));
        strcpy(new->key, key);
        strcpy(new->contents, contents);
        new->next = *head;
        *head = new;
    
    } else {
        next = (*head)->next;
        prev = *head;
        
        while (next != NULL) {
            
            if(strcmp(next->key, key) < 0) {
                prev = next;
                next = next->next;
                
            } else {
                
                new = malloc(sizeof *new );
                new->key = malloc(sizeof(char) * strlen(key));
                new->contents = malloc(sizeof(char) * strlen(contents));
                strcpy(new->key, key);
                strcpy(new->contents, contents);
                prev->next = new;
                new->next = next;
                break;
            }
        }
        
        if (next == NULL) {
            
            new = malloc(sizeof *new );
            new->key = malloc(sizeof(char) * strlen(key));
            new->contents = malloc(sizeof(char) * strlen(contents));
            strcpy(new->key, key);
            strcpy(new->contents, contents);
            new->next = next;
            prev->next = new;
        }
        
    }
    
}


void printCurrent(struct node *n){
    
    if (n == NULL) return;
    
    printf("%s\n", n->key);
    printf("%s\n", n->contents);
    
}



void printAll(struct node *n){
    
    if (n == NULL) return;
    
    printf("%s\n", n->key);
    printf("%s\n", n->contents);
    printAll(n->next);

}

int my_strlen(char *string) //Function to calculate length of given string
{
    int i;
    for(i=0;string[i]!='\0';i++);
    return i;
    
};



int main(int argc, char * argv[])

{
    char tty[40];
    snprintf(tty, 40,"%s", ttyname(STDIN_FILENO));
    
    char uid[20];
    snprintf(uid, 20, "%d", getuid());
    
    int counter = 0;
    int length = 0;
    int i = 1;
    
    for(;i<argc;i++) {
        length = length + my_strlen(argv[i]);
    }
    
    char str[30];
    sprintf(str, "%d", length);
    
    
    
    /*TinyDir setup*/
    
    tinydir_dir dir;
    tinydir_open(&dir, ".");
    
    /*TinyDir parse loop*/
    
    while (dir.has_next) {
        
        tinydir_file file;
        tinydir_readfile(&dir, &file);
        /*Check if file and not directory*/
        if(!file.is_dir) {
            
            counter++;
        }
        /*Go to the next file*/
        tinydir_next(&dir);
    }
    
    /*Close the directory*/
    tinydir_close(&dir);
    /*TinyDir File counts string*/
    char fileNumber[20];
    char *fnp;
    snprintf(fileNumber, 20, "%d", counter);
    fnp = fileNumber;
    
    /*End of TinyDir setup*/
    
    char cwd[256];
    
    uname(&unameData);
    
    char *ttyname(int fd);
    char * pathname;
    pathname = ttyname(0);
    
    char *ttyname(int fildes);
    time_t  time_raw_format;
    struct tm * ptr_time;
    time ( &time_raw_format);
    ptr_time = localtime ( &time_raw_format );
    
    struct node *save = NULL;
    struct node *head = NULL;
    
    insert(&head, "progname", argv[0]);
    insert(&head, "user", getlogin());
    insert(&head, "host", unameData.sysname);
    insert(&head, "uid", uid);
    insert(&head, "tty", tty);
    insert(&head, "date", asctime(ptr_time));
    insert(&head, "cwd", getcwd(cwd, sizeof(cwd)));
    insert(&head, "files", fnp);
    insert(&head, "term", getenv("TERM"));
    insert(&head, "args", str);
    
    
    printf("Type a command, type 'help' to see list of commands, or type 'quit' to quit:");
    
    char user_input[20];
    fgets(user_input, 20, stdin);
    user_input[strlen(user_input) -1] = '\0';
    
    save = head;
    while (strcmp(user_input, "quit") != 0) {
        if (strcmp(user_input, "list all") == 0){
            head = save;
            printAll(head);
            
        } else if (strcmp(user_input, "quit") == 0) {
            
            break;
            
        } else if ((strcmp(user_input, "help") != 0)) {
            
            head = save;
            
            for(;head!=NULL;head=head->next) {
                if ((strcmp(user_input, head->key) == 0)) {
                    
                    printCurrent(head);
                    
                    break;
                }
            }
            
            
            
        } else if ((strcmp(user_input, "help") == 0)){
            
            printf("\nList of different key queries:\n"
                   "progname - the name of the running program\n"
                   "user - the login name of the user\n"
                   "host - the name of the machine the program is running on\n"
                   "uid - the users userid #\n"
                   "tty - the users current terminal\n"
                   "date - the current day and time\n"
                   "cwd - the current working directory\n"
                   "files - the number of files in the current directory\n"
                   "term - the users terminal type\n"
                   "args - the total number of chars of all program arguments\n"
                   "Or type 'list all' to list all of the pairs\n");
        }
        
        printf("\nType a command, type 'help' to see list of commands, or type 'quit' to quit:");
        fgets(user_input, 20, stdin);
        user_input[strlen(user_input) -1] = '\0';
        
    }
    
    return 0;
    
}