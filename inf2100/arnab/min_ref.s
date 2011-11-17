        .globl  main                    
main:   pushl   %ebp                    # Start function main
        movl    %esp,%ebp               
        subl    $4,%esp                 # Get 4 bytes local data space
        movl    $1,%eax                 # 1
        movl    %eax,-4(%ebp)           # k =
        movl    -4(%ebp),%eax           # k
        pushl   %eax                    
        movl    -4(%ebp),%eax           # k
        movl    %eax,%ecx               
        popl    %eax                    
        addl    %ecx,%eax               # Compute +
        jmp     .exit$main              # return-statement
.exit$main:
        addl    $4,%esp                 # Release local data space
        popl    %ebp                    
        ret                             # End function main
