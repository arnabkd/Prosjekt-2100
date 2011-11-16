        .globl  f                       
f:      pushl   %ebp                    # Start function f
        movl    %esp,%ebp               
        movl    $3,%eax                 # 3
        jmp     .exit$f                 # return-statement
.exit$f:                                
        popl    %ebp                    
        ret                             # End function f
        .globl  main                    
main:   pushl   %ebp                    # Start function main
        movl    %esp,%ebp               
        subl    $8,%esp                 # Get 8 bytes local data space
        call    f                       # Call f
        movl    %eax,-4(%ebp)           # b =
        movl    -4(%ebp),%eax           # b
        pushl   %eax                    
        movl    $1,%eax                 # 1
        movl    %eax,%ecx               
        popl    %eax                    
        addl    %ecx,%eax               # Compute +
        movl    %eax,-8(%ebp)           # c =
.exit$main:
        addl    $8,%esp                 # Release local data space
        popl    %ebp                    
        ret                             # End function main
