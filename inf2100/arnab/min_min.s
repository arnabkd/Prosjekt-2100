        .globl  x                       
x:      .fill   4                       
        .globl  main                    
main:   pushl   %ebp                    # int main;
        movl    %esp,%ebp               
        movl    $1,%eax                 # 1
        movl    %eax,x                  # x being assigned
.exit$main:                                
        popl    %ebp                    
        ret                             # end main
