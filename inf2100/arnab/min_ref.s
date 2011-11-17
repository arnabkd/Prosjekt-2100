        .globl  one                     
one:    pushl   %ebp                    # Start function one
        movl    %esp,%ebp               
        movl    $1,%eax                 # 1
        jmp     .exit$one               # return-statement
.exit$one:                                
        popl    %ebp                    
        ret                             # End function one
        .globl  main                    
main:   pushl   %ebp                    # Start function main
        movl    %esp,%ebp               
        subl    $4,%esp                 # Get 4 bytes local data space
        movl    $2,%eax                 # 2
        pushl   %eax                    # Push parameter #2
        movl    $1,%eax                 # 1
        pushl   %eax                    # Push parameter #1
        call    one                     # Call one
        popl    %ecx                    # Pop parameter #1
        popl    %ecx                    # Pop parameter #2
        movl    %eax,-4(%ebp)           # k =
.exit$main:
        addl    $4,%esp                 # Release local data space
        popl    %ebp                    
        ret                             # End function main
