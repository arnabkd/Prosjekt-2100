        .data                           
        .globl  LF                      
LF:     .fill   4                       
        .text                           
        .globl  gcd                     
gcd:    pushl   %ebp                    # int gcd;
        movl    %esp,%ebp               
        movl    8(%ebp),%eax            # Putting a in %eax
        jmp     .exit$gcd               # return-statement for gcd
.exit$gcd:                                
        popl    %ebp                    
        ret                             # end gcd
        .globl  main                    
main:   pushl   %ebp                    # int main;
        movl    %esp,%ebp               
        subl    $8,%esp                 # Allocate 8 bytes
        movl    $10,%eax                # 10
        movl    %eax,LF                 # LF being assigned
        call    getint                  # call getint
        movl    %eax,-4(%ebp)           # v1 being assigned
        call    getint                  # call getint
        movl    %eax,-8(%ebp)           # v2 being assigned
.exit$main:                                
        addl    $8,%esp                 # Free 8 bytes
        popl    %ebp                    
        ret                             # end main
